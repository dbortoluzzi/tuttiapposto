package it.dbortoluzzi.tuttiapposto.framework

import it.dbortoluzzi.data.BookingPersistenceSource
import it.dbortoluzzi.domain.Booking
import it.dbortoluzzi.domain.util.ServiceResult
import it.dbortoluzzi.tuttiapposto.api.ApiHelper
import it.dbortoluzzi.tuttiapposto.api.toServiceResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AndroidBookingSource @Inject constructor(
        var apiHelper: ApiHelper
) : BookingPersistenceSource {

    override suspend fun createBooking(booking: Booking): ServiceResult<Booking> {
        return apiHelper
                .createBooking(booking)
                .toServiceResult()
    }

    companion object {
        private val TAG = "AndroidBookingSource"
    }

}