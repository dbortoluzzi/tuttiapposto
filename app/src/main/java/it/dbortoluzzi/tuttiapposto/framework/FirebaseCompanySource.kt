package it.dbortoluzzi.tuttiapposto.framework

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import it.dbortoluzzi.data.CompanyPersistenceSource
import it.dbortoluzzi.domain.Company
import it.dbortoluzzi.domain.util.ServiceResult
import it.dbortoluzzi.domain.util.ServiceResult.*
import it.dbortoluzzi.tuttiapposto.model.FirebaseCompany
import it.dbortoluzzi.tuttiapposto.model.toObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseCompanySource @Inject constructor(
        var db: FirebaseFirestore
) : CompanyPersistenceSource {

    override suspend fun getActiveCompanies(): ServiceResult<List<Company>> {
        try {
            return when(val resultDocumentSnapshot = db.collection("Company").get().await()) {
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
    }

}