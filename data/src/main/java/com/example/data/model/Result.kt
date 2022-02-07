package com.example.data.model


import com.example.data.db.Movie
import com.google.gson.annotations.SerializedName

data class Result(
    @SerializedName("backdrop_path")
    val backdrop_path: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String
){
    fun toCharacterEntity() = Movie(
        id = id,
        title = title,
        backdrop_path = backdrop_path
    )
}