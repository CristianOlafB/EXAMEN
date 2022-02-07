package com.example.data.repository.firebase


import com.example.data.model.LocationL
import com.example.domain.repository.FirebaseRepositoryLocation
import com.example.domain.util.Resulta
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseRepositoryLocationImpl @Inject constructor(
    dbs: FirebaseFirestore
) : FirebaseRepositoryLocation {
    private val locationCollectionReference = dbs.collection("location")

    override suspend fun saveLocation(
        lactitud: String,
        logintud: String,
        currentdate: String
    ): Resulta {
        return try {
            withContext(Dispatchers.IO) {
                val docRef = locationCollectionReference.add(
                    LocationL(
                        lactitud,
                        logintud,
                        currentdate
                    )
                ).await()
                Resulta.Success(docRef)
            }
        } catch (e: Exception) {
            Resulta.Error(e.message.toString())
        }
    }

}