package com.example.domain.repository

import kotlinx.coroutines.flow.Flow
import androidx.paging.PagingData
import com.example.domain.model.movie.Movies

interface MovieRepository {
    fun getPagedMoviesList(characterName: String): Flow<PagingData<Movies>>
}