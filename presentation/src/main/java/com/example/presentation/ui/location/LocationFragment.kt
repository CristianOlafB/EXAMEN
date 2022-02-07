package com.example.presentation.ui.location


import android.annotation.SuppressLint
import android.content.*
import android.content.ContentValues.TAG
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.data.model.LocationL
import com.example.domain.usecase.Failure
import com.example.presentation.R
import com.example.presentation.databinding.FragmentUbicationBinding
import com.example.presentation.extension.Constants.Constants.ACTION_START_OR_RESUME_SERVICE
import com.example.presentation.extension.Constants.Constants.ACTION_STOP_SERVICE
import com.example.presentation.extension.failure
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class LocationFragment : Fragment(), OnMapReadyCallback {

    private val viewModel by viewModels<LocationViewModel>()

    private var _binding: FragmentUbicationBinding? = null
    private val binding get() = _binding!!

    private lateinit var locationReceiver: LocationReceiver

    // initializing th variable for firebase firestore
    var db = FirebaseFirestore.getInstance()
    private lateinit var googleMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(viewModel) {
            failure(failure, ::handleFailure)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUbicationBinding.inflate(layoutInflater, container, false)

        locationReceiver = LocationReceiver()


        val isServiceRunning = TrackingService.isServiceRunning

        binding.button.text = if (isServiceRunning) {
            getString(R.string.location_pause)
        } else {
            getString(R.string.location_start)
        }

        setUpMapView(savedInstanceState)

        binding.button.setOnClickListener {
            if (binding.button.text.toString() == getString(R.string.location_start)) {
                binding.button.text = getString(R.string.location_pause)
                sendServiceCommand(ACTION_START_OR_RESUME_SERVICE)
            } else if (binding.button.text.toString() == getString(R.string.location_pause)) {
                binding.button.text = getString(R.string.location_start)
                sendServiceCommand(ACTION_STOP_SERVICE)

            }
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        binding.mapView.onResume()
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(
            locationReceiver,
            IntentFilter("intent_broadcast_location")
        )
    }

    override fun onPause() {
        super.onPause()
        binding.mapView.onPause()
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(locationReceiver)
    }

    private fun sendServiceCommand(action: String) {
        Intent(requireContext(), TrackingService::class.java).also {
            it.action = action
            requireContext().startService(it)
        }
    }

    private fun setUpMapView(savedInstanceState: Bundle?) {
        var mapViewBundle: Bundle? = null
        savedInstanceState?.let {
            mapViewBundle = savedInstanceState.getBundle("map_view_bundle_key")
        }
        binding.mapView.onCreate(mapViewBundle)
        binding.mapView.getMapAsync(this)
        //geoApiContext = GeoApiContext.Builder().apiKey("AIzaSyBuOJTW3OsxIo61u5JtN5iPpe1fnqOKgFc").build()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        var mapViewBundle = outState.getBundle("map_view_bundle_key")
        if (mapViewBundle == null) {
            mapViewBundle = Bundle()
            outState.putBundle("map_view_bundle_key", mapViewBundle)
        }
        //TODO do something here
        binding.mapView.onSaveInstanceState(mapViewBundle)
    }

    @SuppressLint("MissingPermission")
    override fun onMapReady(map: GoogleMap?) {
        map?.isMyLocationEnabled = true
        map?.let {
            googleMap = it
        }
        setCameraView()
    }
    /*
    No se te olvide agregarlo a los uses cases
     */
    private fun setCameraView() {
        db.collection("location")
            .addSnapshotListener { value, e ->
                if (e != null) {
                    Timber.tag(TAG).w(e, "Listen failed.")
                    return@addSnapshotListener
                }
                for (doc in value!!) {
                    val post = doc.toObject(LocationL::class.java)
                    val uno = LatLng(post.latitude.toDouble(), post.longitude.toDouble())
                    googleMap.addMarker(MarkerOptions().position(uno).title(post.currentdate))
                    googleMap.animateCamera(CameraUpdateFactory.newLatLng(uno))
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(uno, 11f))
                }
            }
    }

    private fun handleFailure(failure: Failure?) {
        when (failure) {
            is Failure.NetworkConnection -> renderFailure(R.string.no_network)
            is Failure.ServerError -> renderFailure(R.string.no_network)
            else -> renderFailure(R.string.no_server)
        }
    }

    private fun renderFailure(@StringRes message: Int) {
        notify(message)
    }

    private fun notify(@StringRes message: Int) =
        Snackbar.make(requireView(), message, Snackbar.LENGTH_SHORT).show()

    private inner class LocationReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            println("haces esto")
        }
    }

    override fun onStart() {
        super.onStart()
        binding.mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        binding.mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding.mapView.onLowMemory()
    }

}