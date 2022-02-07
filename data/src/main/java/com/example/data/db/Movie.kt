package com.example.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.domain.model.movie.Movies

@Entity(tableName = "movies")
data class Movie(
    @PrimaryKey
    val id: Int,
    val title: String?,
    val backdrop_path: String?
){
    fun toCharacterEntity() = Movies(
         id = id,
         title = title,
         backdrop_path = backdrop_path,
    )
}