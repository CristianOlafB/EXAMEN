package com.example.data.di

import com.example.data.db.AppDatabase
import com.example.data.db.MovieDao
import com.example.data.db.RemoteKeyDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {

    @Provides
    fun provideMovieDao(database: AppDatabase): MovieDao {
        return database.movieDao()
    }

    @Provides
    fun provideKeysDao(database: AppDatabase): RemoteKeyDao {
        return database.remoteKeysDao()
    }
}