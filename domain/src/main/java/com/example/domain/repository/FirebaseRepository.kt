package com.example.domain.repository

import com.example.domain.util.Resulta

interface FirebaseRepository {
    suspend fun savePhoto(title: String, imageUri: String): Resulta
}