package com.example.data.api

import com.example.data.model.Test
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("discover/movie")
    suspend fun getMovies(
        @Query("page") page: Int
    ):Test

}