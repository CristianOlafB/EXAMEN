package com.example.domain.usecase.photo

import com.example.domain.repository.FirebaseRepository
import com.example.domain.util.Resulta
import javax.inject.Inject

class PostPhotoUseCase @Inject constructor(
    val repository: FirebaseRepository
) {

    suspend operator fun invoke(
        title: String,
        imageUri: String?
    ): Resulta {
        return when {
            isDataValid(title, imageUri) -> {
                repository.savePhoto(title, imageUri!!)
            }
            imageUri == null -> {
                Resulta.Error("Please select a photo")
            }
            else -> {
                Resulta.Error("Fields cannot be empty")
            }
        }
    }

    private fun isDataValid(title: String, imageUri: String?) =
        title.trim().isNotEmpty() && (imageUri != null)

}