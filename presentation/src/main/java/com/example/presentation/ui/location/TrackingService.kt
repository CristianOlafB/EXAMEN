package com.example.presentation.ui.location

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import com.example.domain.usecase.location.LocalCases
import com.example.presentation.extension.Constants.Constants.ACTION_PAUSE_SERVICE
import com.example.presentation.extension.Constants.Constants.ACTION_START_OR_RESUME_SERVICE
import com.example.presentation.extension.Constants.Constants.ACTION_STOP_SERVICE
import com.example.presentation.extension.Constants.Constants.NOTIFICATION_CHANNEL_ID
import com.example.presentation.extension.Constants.Constants.NOTIFICATION_CHANNEL_NAME
import com.example.presentation.extension.Constants.Constants.NOTIFICATION_ID
import com.example.presentation.extension.hasLocationPermissions
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class TrackingService : LifecycleService() {

    @Inject
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    @Inject
    lateinit var notificationManager: NotificationManager

    @Inject
    lateinit var notificationBuilder: NotificationCompat.Builder

    @Inject
    lateinit var notificationObserver: Observer<Long>

    @Inject
    lateinit var localCases: LocalCases

    lateinit var locationCallback: LocationCallback

    private var timeWalkInSeconds = MutableLiveData<Long>()
    private var timeWalkInMillis: Long = 0L
    private var isTracking: Boolean = false
    private var lastSecondTimestamp = 0L

    companion object {
        var isServiceRunning = false
    }

    private fun postInitialValues() {
        Log.d("fechaactual", "")
        timeWalkInSeconds.postValue(0L)
        timeWalkInMillis = 0L
        isTracking = false
        lastSecondTimestamp = 0L
    }

    override fun onCreate() {
        super.onCreate()
        postInitialValues()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale("es", "ES"))
                sdf.timeZone = TimeZone.getTimeZone("America/Mexico_City")
                val currentdate = sdf.format(Date())
                val lastLocation = locationResult.lastLocation
                val latitude = lastLocation.latitude.toBigDecimal().toPlainString()
                val longitude = lastLocation.longitude.toBigDecimal().toPlainString()
                startForegroundService()
                CoroutineScope(Dispatchers.Main).launch {
                    localCases.postLocationUseCase(latitude, longitude, currentdate)
                }
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when (it.action) {
                ACTION_START_OR_RESUME_SERVICE -> {
                    subscribeToLocationUpdates()
                }
                ACTION_PAUSE_SERVICE -> Timber.d("ACTION_PAUSE_SERVICE")
                ACTION_STOP_SERVICE -> {
                    stopService()
                }
            }
        }
        return super.onStartCommand(intent, flags, START_NOT_STICKY)
    }

    override fun onDestroy() {
        super.onDestroy()
        isServiceRunning = false
    }

    private fun stopService() {
        isServiceRunning = false
        postInitialValues()
        timeWalkInSeconds.removeObserver(notificationObserver)
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        stopForeground(true)
        stopSelf()
    }

    private fun startForegroundService() {
        isTracking = true
        isServiceRunning = true

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotification(notificationManager)
        }
        startForeground(NOTIFICATION_ID, notificationBuilder.build())
        timeWalkInSeconds.observe(this, notificationObserver)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotification(notificationManager: NotificationManager) {
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_LOW
        )
        notificationManager.createNotificationChannel(channel)
    }

    @SuppressLint("MissingPermission")
    private fun subscribeToLocationUpdates() {
        if (hasLocationPermissions(this)) {
            val locationRequest = LocationRequest.create().apply {
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                interval = 60000
            }
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest, locationCallback,
                Looper.getMainLooper()
            )
        }
    }

}