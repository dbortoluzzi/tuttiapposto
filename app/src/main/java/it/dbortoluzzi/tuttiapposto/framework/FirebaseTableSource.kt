package it.dbortoluzzi.tuttiapposto.framework

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import it.dbortoluzzi.data.TablePersistenceSource
import it.dbortoluzzi.domain.Table
import it.dbortoluzzi.domain.util.ServiceResult
import it.dbortoluzzi.domain.util.ServiceResult.Error
import it.dbortoluzzi.domain.util.ServiceResult.Success
import it.dbortoluzzi.tuttiapposto.model.FirebaseTable
import it.dbortoluzzi.tuttiapposto.model.toObject
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseTableSource @Inject constructor(
        var db: FirebaseFirestore,
        private val cacheEnabled: Boolean
) : TablePersistenceSource {

    private val timedCache = TimedCache.expiringEvery<List<Table>>(5, TimeUnit.MINUTES)

    override suspend fun getActiveTables(): ServiceResult<List<Table>> {
        return if (cacheEnabled) {
            timedCache.getOrElse(ALL) { -> execGetActiveTables() };
        } else {
            execGetActiveTables()
        }
    }

    override suspend fun getActiveTablesByCompanyIdAndBuildingIdAndRoomId(companyId: String, buildingId: String, roomId: String): ServiceResult<List<Table>> {
        return if (cacheEnabled) {
            timedCache.getOrElse("${companyId}_${buildingId}_${roomId}") { -> execGetActiveTablesByCompanyIdAndBuildingIdAndRoomId(companyId, buildingId, roomId) };
        } else {
            execGetActiveTablesByCompanyIdAndBuildingIdAndRoomId(companyId, buildingId, roomId)
        }
    }

    private suspend fun execGetActiveTables(): ServiceResult<List<Table>> {
        try {
            return when (val resultDocumentSnapshot = db.collection(COLLECTION).whereEqualTo("active", true).get().await()) {
                is Success -> {
                    val rooms = resultDocumentSnapshot.data!!
                            .toObjects(FirebaseTable::class.java)
                            .map { it.toObject() }
                    Success(rooms)
                }
                is Error -> {
                    Log.e(TAG, "getActiveTables ${resultDocumentSnapshot.exception}")
                    Error(resultDocumentSnapshot.exception)
                }
                else -> throw NotImplementedError()
            }
        } catch (exception: Exception) {
            return Error(exception)
        }
    }

    private suspend fun execGetActiveTablesByCompanyIdAndBuildingIdAndRoomId(companyId: String, buildingId: String, roomId: String): ServiceResult<List<Table>> {
        try {
            return when (val resultDocumentSnapshot = db.collection(COLLECTION)
                    .whereEqualTo("active", true)
                    .whereEqualTo("companyId", companyId)
                    .whereEqualTo("buildingId", buildingId)
                    .whereEqualTo("roomId", roomId)
                    .get()
                    .await()
            ) {
                is Success -> {
                    val rooms = resultDocumentSnapshot.data!!
                            .toObjects(FirebaseTable::class.java)
                            .map { it.toObject() }
                    Success(rooms)
                }
                is Error -> {
                    Log.e(TAG, "getActiveTablesByCompanyIdAndBuildingIdAndRoomId ${resultDocumentSnapshot.exception}")
                    Error(resultDocumentSnapshot.exception)
                }
                else -> throw NotImplementedError()
            }
        } catch (exception: Exception) {
            return Error(exception)
        }
    }

    companion object {
        private val TAG = "FirebaseTableSource"
        private val ALL = "ALL"
        private val COLLECTION = "Tables"
    }

}