package it.dbortoluzzi.tuttiapposto.framework

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import it.dbortoluzzi.data.BuildingPersistenceSource
import it.dbortoluzzi.data.CompanyPersistenceSource
import it.dbortoluzzi.domain.Building
import it.dbortoluzzi.domain.Company
import it.dbortoluzzi.domain.util.ServiceResult
import it.dbortoluzzi.domain.util.ServiceResult.*
import it.dbortoluzzi.tuttiapposto.model.FirebaseBuilding
import it.dbortoluzzi.tuttiapposto.model.FirebaseCompany
import it.dbortoluzzi.tuttiapposto.model.toObject
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseBuildingSource @Inject constructor(
        var db: FirebaseFirestore,
        private val cacheEnabled: Boolean
) : BuildingPersistenceSource {

    private val timedCache = TimedCache.expiringEvery<List<Building>>(1, TimeUnit.MINUTES)

    override suspend fun getActiveBuildings(): ServiceResult<List<Building>> {
        return if (cacheEnabled) {
            timedCache.getOrElse(ALL) { -> execGetActiveBuildings() };
        } else {
            execGetActiveBuildings()
        }
    }

    override suspend fun getActiveBuildingsByCompanyId(companyId: String): ServiceResult<List<Building>> {
        return if (cacheEnabled) {
            timedCache.getOrElse(companyId) { -> execGetActiveBuildingsByCompanyId(companyId) };
        } else {
            execGetActiveBuildingsByCompanyId(companyId)
        }
    }

    private suspend fun execGetActiveBuildingsByCompanyId(companyId: String): ServiceResult<List<Building>> {
        try {
            return when (val resultDocumentSnapshot = db.collection(COLLECTION)
                    .whereEqualTo("active", true)
                    .whereEqualTo("companyId", companyId)
                    .get()
                    .await()
            ) {
                is Success -> {
                    val buildings = resultDocumentSnapshot.data!!
                            .toObjects(FirebaseBuilding::class.java)
                            .map { it.toObject() }
                    Success(buildings)
                }
                is Error -> {
                    Log.e(TAG, "getActiveBuildingsByCompanyId ${resultDocumentSnapshot.exception}")
                    Error(resultDocumentSnapshot.exception)
                }
                else -> throw NotImplementedError()
            }
        } catch (exception: Exception) {
            return Error(exception)
        }
    }

    private suspend fun execGetActiveBuildings(): ServiceResult<List<Building>> {
        try {
            return when (val resultDocumentSnapshot = db.collection(COLLECTION).whereEqualTo("active", true).get().await()) {
                is Success -> {
                    val buildings = resultDocumentSnapshot.data!!
                            .toObjects(FirebaseBuilding::class.java)
                            .map { it.toObject() }
                    Success(buildings)
                }
                is Error -> {
                    Log.e(TAG, "getActiveBuildings ${resultDocumentSnapshot.exception}")
                    Error(resultDocumentSnapshot.exception)
                }
                else -> throw NotImplementedError()
            }
        } catch (exception: Exception) {
            return Error(exception)
        }
    }

    companion object {
        private val TAG = "FirebaseBuildingSource"
        private val ALL = "ALL"
        private val COLLECTION = "Buildings"
    }

}