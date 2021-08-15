package it.dbortoluzzi.tuttiapposto.framework

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import it.dbortoluzzi.domain.util.ServiceResult
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

class FirebaseUserSource constructor(
        var db: FirebaseFirestore
) : UserPersistenceSource {

    override suspend fun saveUser(userId: String, email: String): ServiceResult<Boolean> {
        val user = hashMapOf(
                "email" to email
        )

        val documentResult: ServiceResult<Boolean> = db.collection("Users").document(userId)
                .set(user)
                .awaitAny()
        return when (documentResult) {
            is ServiceResult.Success -> ServiceResult.Success(true)
            is ServiceResult.Error -> {
                Log.e(TAG, "saveUser ${documentResult.exception}")
                ServiceResult.Error(documentResult.exception)
            }
            else -> throw NotImplementedError()
        }
    }

    companion object {
        private val TAG = "FirebaseUserSource"
        private val COLLECTION = "Users"
    }

}

interface UserPersistenceSource {

    suspend fun saveUser(userId: String, email: String): ServiceResult<Boolean>

}