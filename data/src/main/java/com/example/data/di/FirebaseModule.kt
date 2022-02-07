package com.example.data.di

import com.example.domain.repository.FirebaseRepository
import com.example.domain.repository.FirebaseRepositoryLocation
import com.example.domain.usecase.location.LocalCases
import com.example.domain.usecase.location.PostLocationUseCase
import com.example.domain.usecase.photo.PhotoCases
import com.example.domain.usecase.photo.PostPhotoUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class FirebaseModule {

    @Provides
    @Singleton
    fun provideFirebaseAuth() = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseDB() = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseStorage() = FirebaseStorage.getInstance()

    @Provides
    @Singleton
    fun provideNoteUseCases(repository: FirebaseRepository): PhotoCases {
        return PhotoCases(
            postNoteUseCase = PostPhotoUseCase(repository)
        )
    }

    @Provides
    @Singleton
    fun provideLocationUseCases(repositor: FirebaseRepositoryLocation): LocalCases {
        return LocalCases(
            postLocationUseCase = PostLocationUseCase(repositor)
        )
    }

}