package com.example.data.repository.movie

import androidx.paging.*
import com.example.domain.repository.MovieRepository
import com.example.data.api.ApiService
import com.example.data.db.AppDatabase
import com.example.domain.model.movie.Movies
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@ExperimentalPagingApi
class MovieRepositoryImpl(
    private val apiService: ApiService,
    private val db: AppDatabase
) : MovieRepository {

    override fun getPagedMoviesList(search: String): Flow<PagingData<Movies>> {
        val pagingSourceFactory = { db.movieDao().getPhotos() }
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            remoteMediator = MoviesRemoteMediator(
                apiService,
                db
            ),
            pagingSourceFactory = pagingSourceFactory
        ).flow.map { CharacterEntityPagingData ->
            CharacterEntityPagingData.map { characterEntity -> characterEntity.toCharacterEntity() }
        }
    }

}