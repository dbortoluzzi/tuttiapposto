package it.dbortoluzzi.tuttiapposto.framework

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import it.dbortoluzzi.data.AvailabilityReportSource
import it.dbortoluzzi.domain.AvailabilityReport
import it.dbortoluzzi.domain.util.ServiceResult
import it.dbortoluzzi.domain.util.ServiceResult.Error
import it.dbortoluzzi.domain.util.ServiceResult.Success
import javax.inject.Inject
import javax.inject.Singleton

class FirebaseAvailabilityReportSource constructor(
        var db: FirebaseFirestore,
        private val cacheEnabled: Boolean
) : AvailabilityReportSource {

    override suspend fun createAvailabilityReport(availabilityReport: AvailabilityReport): ServiceResult<AvailabilityReport> {
        try {
            return when (val resultDocumentSnapshot = db.collection(COLLECTION)
                    .add(availabilityReport)
                    .await()
            ) {
                is Success -> {
                    val entityReference = resultDocumentSnapshot.data!!
                    Success(AvailabilityReport(entityReference.id, availabilityReport))
                }
                is Error -> {
                    Log.e(TAG, "createAvailabilityReport ${resultDocumentSnapshot.exception}")
                    Error(resultDocumentSnapshot.exception)
                }
                else -> throw NotImplementedError()
            }
        } catch (exception: Exception) {
            return Error(exception)
        }
    }

    companion object {
        private val TAG = "FirebaseAvailabilityReportSource"
        private val ALL = "ALL"
        private val COLLECTION = "AvailabilityReports"
    }

}