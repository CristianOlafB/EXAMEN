package com.example.domain.repository

import com.example.domain.util.Resulta

interface FirebaseRepositoryLocation {
    suspend fun saveLocation(lactitud: String, logintud: String, currentdate: String): Resulta
}