package com.example.presentation.ui.photo

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.photo.PhotoCases
import com.example.domain.util.Resulta
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotoViewModel @Inject constructor(
    private val notePhotoCases: PhotoCases,
    private val app: Application
) : ViewModel() {

    private val _isLoading = MutableLiveData(false)
    val isLoading = _isLoading
    var result = MutableLiveData<Resulta>()

    fun resetResult() {
        result = MutableLiveData<Resulta>()
    }

    fun saveNote(title: String, imageUri: String?) =
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.postValue(true)
            try {
                if (isNetworkAvailable(app)) {
                    val apiResult =
                        notePhotoCases.postNoteUseCase(title, imageUri)
                    result.postValue(apiResult)
                    _isLoading.postValue(false)
                } else {
                    _isLoading.postValue(false)
                    result.postValue(Resulta.Error("Internet is not available"))
                }
            } catch (exceptio: Exception) {
                _isLoading.postValue(false)
                result.postValue(Resulta.Error(exceptio.message.toString()))
            }
        }

    private fun isNetworkAvailable(context: Context) =
        (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).run {
            getNetworkCapabilities(activeNetwork)?.run {
                hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                        || hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                        || hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)
            } ?: false
        }

}