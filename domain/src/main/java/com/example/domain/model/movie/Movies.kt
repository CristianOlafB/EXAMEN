package com.example.domain.model.movie

import kotlinx.android.parcel.Parcelize

@Parcelize
data class Movies(
    val id : Int,
    val title: String?,
    val backdrop_path: String?
){
    companion object{
        const val HIGH_IMG = "https://image.tmdb.org/t/p/w500"
    }
}