package it.dbortoluzzi.tuttiapposto.framework

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import it.dbortoluzzi.data.RoomPersistenceSource
import it.dbortoluzzi.domain.Building
import it.dbortoluzzi.domain.Company
import it.dbortoluzzi.domain.Room
import it.dbortoluzzi.domain.util.ServiceResult
import it.dbortoluzzi.domain.util.ServiceResult.Error
import it.dbortoluzzi.domain.util.ServiceResult.Success
import it.dbortoluzzi.tuttiapposto.model.FirebaseRoom
import it.dbortoluzzi.tuttiapposto.model.toObject
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseRoomSource @Inject constructor(
        var db: FirebaseFirestore,
        private val cacheEnabled: Boolean
) : RoomPersistenceSource {

    private val timedCache = TimedCache.expiringEvery<List<Room>>(5, TimeUnit.MINUTES)

    override suspend fun getActiveRooms(): ServiceResult<List<Room>> {
        return if (cacheEnabled) {
            timedCache.getOrElse(ALL) { -> execGetActiveRooms() };
        } else {
            execGetActiveRooms()
        }
    }

    override suspend fun getActiveRoomsByCompanyIdAndBuildingId(companyId: String, buildingId: String): ServiceResult<List<Room>> {
        return if (cacheEnabled) {
            timedCache.getOrElse("${companyId}_${buildingId}") { -> execGetActiveRoomsByCompanyIdAndBuildingId(companyId, buildingId) };
        } else {
            execGetActiveRoomsByCompanyIdAndBuildingId(companyId, buildingId)
        }
    }

    private suspend fun execGetActiveRooms(): ServiceResult<List<Room>> {
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

    private suspend fun execGetActiveRoomsByCompanyIdAndBuildingId(companyId: String, buildingId: String): ServiceResult<List<Room>> {
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
        private val ALL = "ALL"
        private val COLLECTION = "Rooms"
    }

}