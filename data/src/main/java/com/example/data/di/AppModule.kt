package com.example.data.di

import android.content.Context
import androidx.room.Room
import com.example.data.BuildConfig
import com.example.data.api.AuthHeaderInterceptor
import com.example.data.db.AppDatabase
import com.example.data.util.ErrorHandlerImpl
import com.example.domain.model.state.ErrorHandler
import com.squareup.moshi.Moshi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    abstract fun bindErrorHandler(errorHandlerImpl: ErrorHandlerImpl): ErrorHandler

    @Binds
    abstract fun provideAuthHeaderInterceptor(
        authInterceptor: AuthHeaderInterceptor
    ): Interceptor

    companion object {

        @Provides
        @Singleton
        fun providesRoomDb(
            @ApplicationContext context: Context
        ) = Room.databaseBuilder(context,AppDatabase::class.java,"databaseNew")
            .fallbackToDestructiveMigration()
            .build()

        @Provides
        @Singleton
        fun provideRetrofit(
            okHttpClient: OkHttpClient,
            @MoshiNetwork moshi: Moshi
        ): Retrofit {
            return Retrofit.Builder().baseUrl(BuildConfig.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
        }

        @Provides
        fun provideOkHttpClient(interceptor: Interceptor): OkHttpClient {
            val builder = OkHttpClient().newBuilder().addInterceptor(interceptor)

            if (BuildConfig.DEBUG) builder.addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })

            return builder.build()
        }

        @Provides
        @MoshiDefault
        fun provideMoshi(
            builder: Moshi.Builder
        ): Moshi = builder.build()

        @Provides
        fun provideMoshiBuilder() = Moshi.Builder()

    }

}