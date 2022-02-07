package com.example.data.repository.firebase

import android.net.Uri
import com.example.data.model.SnapShot
import com.example.domain.repository.FirebaseRepository
import com.example.domain.util.Resulta
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

@Singleton
class FirebaseRepositoryImpl @Inject constructor(
    db: FirebaseFirestore,
    private val storageInstance: FirebaseStorage
) : FirebaseRepository {
    private val storageReference = storageInstance.reference
    private val notebookCollectionReference = db.collection("users")

    override suspend fun savePhoto(
        title: String,
        imageUri: String
    ): Resulta {
        return try {
            withContext(Dispatchers.IO) {
                val uri = Uri.parse(imageUri)
                val filePath =
                    storageReference.child("snapshot").child("my_image_${Timestamp.now().seconds}")
                filePath.putFile(uri).await()

                val docRef = notebookCollectionReference.add(
                    SnapShot(
                        "1",
                        title,
                        filePath.downloadUrl.await().toString()
                    )
                ).await()

                Resulta.Success(docRef)
            }
        } catch (e: Exception) {
            Resulta.Error(e.message.toString())
        }
    }

}