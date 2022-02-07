package com.example.data.di

import com.example.data.repository.firebase.FirebaseRepositoryImpl
import com.example.data.repository.firebase.FirebaseRepositoryLocationImpl
import com.example.domain.repository.FirebaseRepository
import com.example.domain.repository.FirebaseRepositoryLocation
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryFirebaseModule {
    @Singleton
    @Provides
    fun provideFirebaseRepository(
        db: FirebaseFirestore,
        storage: FirebaseStorage
    ): FirebaseRepository {
        return FirebaseRepositoryImpl(
            db,
            storage
        )
    }

    @Singleton
    @Provides
    fun provideFirebaseRepositoryLocation(
        db: FirebaseFirestore
    ): FirebaseRepositoryLocation {
        return FirebaseRepositoryLocationImpl(
            db
        )
    }

}