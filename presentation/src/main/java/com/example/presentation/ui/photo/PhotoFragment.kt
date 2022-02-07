package com.example.presentation.ui.photo


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.domain.util.Resulta
import com.example.presentation.R
import com.example.presentation.databinding.FragmentPhotoBinding
import com.example.presentation.extension.Constants.Companion.GALLERY_CODE
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PhotoFragment : Fragment() {

    private val viewModel by viewModels<PhotoViewModel>()

    private var _binding: FragmentPhotoBinding? = null
    private val binding get() = _binding!!

    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println("Beginning")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhotoBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.isLoading.observe(
            viewLifecycleOwner, { response ->
                if (response) {
                    binding.postProgressBar.visibility = View.VISIBLE
                } else {
                    binding.postProgressBar.visibility = View.INVISIBLE
                }
            }
        )

        binding.ivHeader.setOnClickListener {
            getImage()
        }

        binding.btnPostSave.setOnClickListener {
            if (imageUri != null && binding.etPostTitle.text.toString().isNotEmpty()) {
                viewModel.saveNote(binding.etPostTitle.text.toString(), imageUri.toString())
            } else {
                handleSaveResponse(Resulta.Error(getString(R.string.check_data)))
            }
        }


        binding.ivHeader.setBackgroundResource(R.drawable.ic_camera)

        viewModel.result.observe(
            viewLifecycleOwner, {
                handleSaveResponse(it)
                viewModel.resetResult()
                binding.etPostTitle.setText("")
                binding.ivHeader.setBackgroundResource(R.drawable.ic_camera)
            })

    }

    private fun handleSaveResponse(result: Resulta) {
        when (result) {
            is Resulta.Success -> {
                Toast.makeText(context, R.string.correcto, Toast.LENGTH_LONG).show()
            }
            is Resulta.Error -> {
                Toast.makeText(context, R.string.check_data, Toast.LENGTH_LONG).show()
            }
            else -> Toast.makeText(context, R.string.error, Toast.LENGTH_LONG).show()
        }
    }

    /*
    no se te olvide poner la nueva version COB
     */
    fun getImage() {
        startActivityForResult(
            Intent(Intent.ACTION_GET_CONTENT)
                .setType("image/*"), GALLERY_CODE
        )
    }

    /*
        no se te olvide poner la nueva version COB
         */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GALLERY_CODE && resultCode == AppCompatActivity.RESULT_OK) {
            if (data != null) {
                imageUri = data.data
                binding.ivHeader.setImageURI(imageUri)
            }
        }
    }

}