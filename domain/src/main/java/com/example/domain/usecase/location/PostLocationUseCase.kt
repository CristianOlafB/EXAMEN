package com.example.domain.usecase.location

import com.example.domain.repository.FirebaseRepositoryLocation
import com.example.domain.util.Resulta
import javax.inject.Inject

class PostLocationUseCase @Inject constructor(
    val firebaseRepositoryLocation: FirebaseRepositoryLocation
) {
    suspend operator fun invoke(
        lactitud: String,
        logintud: String,
        currentdate: String
    ): Resulta {
        return when {
            isDataValidLocation(lactitud, logintud) -> {
                firebaseRepositoryLocation.saveLocation(lactitud, logintud, currentdate)
            }
            false -> {
                Resulta.Error("Please select a photo")
            }
            else -> {
                Resulta.Error("Fields cannot be empty")
            }
        }
    }

    private fun isDataValidLocation(title: String, imageUri: String?) =
        title.trim().isNotEmpty() && (imageUri != null)
}