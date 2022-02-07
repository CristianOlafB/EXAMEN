package com.example.data.di

import androidx.paging.ExperimentalPagingApi
import com.example.data.api.ApiService
import com.example.data.db.AppDatabase
import com.example.data.repository.movie.MovieRepositoryImpl
import com.example.domain.repository.MovieRepository
import com.example.domain.usecase.movie.GetMoviesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideGetCharactersByNameUseCase(
        getCharactersRepository: MovieRepository
    ): GetMoviesUseCase = GetMoviesUseCase(getCharactersRepository)

    @Singleton
    @Provides
    @ExperimentalPagingApi
    fun provideCharacterRepository(
        api: ApiService,
        db: AppDatabase
    ): MovieRepository = MovieRepositoryImpl(api, db)

}