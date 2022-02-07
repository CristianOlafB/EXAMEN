package com.example.domain.usecase.movie


import androidx.paging.PagingData
import com.example.domain.model.movie.Movies
import com.example.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow

class GetMoviesUseCase (
    private val movieRepository: MovieRepository
){
    operator fun invoke(characterName: String): Flow<PagingData<Movies>> = movieRepository.getPagedMoviesList(characterName)
}