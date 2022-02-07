package com.example.domain.util

sealed class Resulta {
    data class Success(var data: Any? = null, var message: String? = null) : Resulta()
    data class Error(var message: String) : Resulta()
    object Loading: Resulta()
}
