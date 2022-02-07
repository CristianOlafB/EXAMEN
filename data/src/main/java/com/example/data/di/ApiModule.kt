package com.example.data.di

import com.example.data.api.ApiService
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @MoshiNetwork
    fun provideMoshi(builder: Moshi.Builder): Moshi {
        return builder
            .build()
    }

    @Provides
    fun provideMovieService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}