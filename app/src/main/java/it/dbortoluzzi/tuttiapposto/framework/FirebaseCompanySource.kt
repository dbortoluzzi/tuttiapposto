package it.dbortoluzzi.tuttiapposto.framework

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import it.dbortoluzzi.data.CompanyPersistenceSource
import it.dbortoluzzi.domain.Company
import it.dbortoluzzi.domain.util.ServiceResult
import it.dbortoluzzi.domain.util.ServiceResult.*
import it.dbortoluzzi.tuttiapposto.model.FirebaseCompany
import it.dbortoluzzi.tuttiapposto.model.toObject
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseCompanySource @Inject constructor(
        var db: FirebaseFirestore,
        private val cacheEnabled: Boolean
) : CompanyPersistenceSource {

    private val timedCache = TimedCache.expiringEvery<List<Company>>(5, TimeUnit.MINUTES)

    override suspend fun getActiveCompanies(): ServiceResult<List<Company>> {
        return if (cacheEnabled) {
            timedCache.getOrElse(ALL) { -> execGetActiveCompanies() };
        } else {
            execGetActiveCompanies()
        }
    }

    private suspend fun execGetActiveCompanies(): ServiceResult<List<Company>> {
        try {
            return when(val resultDocumentSnapshot = db.collection(COLLECTION).whereEqualTo("active", true).get().await()) {
                is Success -> {
                    val companies = resultDocumentSnapshot.data!!
                            .toObjects(FirebaseCompany::class.java)
                            .map { it.toObject() }
                    Success(companies)
                }
                is Error -> {
                    Log.e(TAG, "getActiveCompanies ${resultDocumentSnapshot.exception}")
                    Error(resultDocumentSnapshot.exception)
                }
                else -> throw NotImplementedError()
            }
        }
        catch (exception: Exception) {
            return Error(exception)
        }
    }

    companion object {
        private val TAG = "FirebaseCompanySource"
        private val ALL = "ALL"
        private val COLLECTION = "Companies"
    }

}