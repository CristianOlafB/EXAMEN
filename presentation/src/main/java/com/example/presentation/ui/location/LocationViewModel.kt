package com.example.presentation.ui.location

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.Failure
import com.example.domain.usecase.location.LocalCases
import com.example.domain.usecase.location.PostLocationUseCase
import com.example.domain.util.Resulta
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val localCases: LocalCases,
    private val app: Application
) : ViewModel() {

    private val _failure: MutableLiveData<Failure> = MutableLiveData()
    val failure: LiveData<Failure> = _failure

    private fun handleFailure(failure: Failure) {
        _failure.value = failure
    }


}