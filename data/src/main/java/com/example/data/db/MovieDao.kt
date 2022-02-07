package com.example.data.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPhotos(photos: List<Movie>)

    @Query("DELETE FROM movies")
    fun deleteAllPhotos()

    @Query("SELECT * FROM movies")
    fun getPhotos(): PagingSource<Int, Movie>


}