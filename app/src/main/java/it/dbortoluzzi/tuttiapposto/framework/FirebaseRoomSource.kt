package it.dbortoluzzi.tuttiapposto.framework

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import it.dbortoluzzi.data.RoomPersistenceSource
import it.dbortoluzzi.domain.Room
import it.dbortoluzzi.domain.util.ServiceResult
import it.dbortoluzzi.domain.util.ServiceResult.Error
import it.dbortoluzzi.domain.util.ServiceResult.Success
import it.dbortoluzzi.tuttiapposto.model.FirebaseRoom
import it.dbortoluzzi.tuttiapposto.model.toObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseRoomSource @Inject constructor(
        var db: FirebaseFirestore,
) : RoomPersistenceSource {

    override suspend fun getActiveRooms(): ServiceResult<List<Room>> {
        try {
            return when (val resultDocumentSnapshot = db.collection(COLLECTION).whereEqualTo("active", true).get().await()) {
                is Success -> {
                    val rooms = resultDocumentSnapshot.data!!
                            .toObjects(FirebaseRoom::class.java)
                            .map { it.toObject() }
                    Success(rooms)
                }
                is Error -> {
                    Log.e(TAG, "getActiveRooms ${resultDocumentSnapshot.exception}")
                    Error(resultDocumentSnapshot.exception)
                }
                else -> throw NotImplementedError()
            }
        } catch (exception: Exception) {
            return Error(exception)
        }
    }

    override suspend fun getActiveRoomsByCompanyIdAndBuildingId(companyId: String, buildingId: String): ServiceResult<List<Room>> {
        try {
            return when (val resultDocumentSnapshot = db.collection(COLLECTION)
                    .whereEqualTo("active", true)
                    .whereEqualTo("companyId", companyId)
                    .whereEqualTo("buildingId", buildingId)
                    .get()
                    .await()
            ) {
                is Success -> {
                    val rooms = resultDocumentSnapshot.data!!
                            .toObjects(FirebaseRoom::class.java)
                            .map { it.toObject() }
                    Success(rooms)
                }
                is Error -> {
                    Log.e(TAG, "getActiveRoomsByCompanyIdAndBuildingId ${resultDocumentSnapshot.exception}")
                    Error(resultDocumentSnapshot.exception)
                }
                else -> throw NotImplementedError()
            }
        } catch (exception: Exception) {
            return Error(exception)
        }
    }

    companion object {
        private val TAG = "FirebaseRoomSource"
        private val COLLECTION = "Rooms"
    }

}