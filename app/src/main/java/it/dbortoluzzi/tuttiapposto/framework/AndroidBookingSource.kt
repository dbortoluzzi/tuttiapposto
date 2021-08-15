package it.dbortoluzzi.tuttiapposto.framework

import it.dbortoluzzi.data.BookingPersistenceSource
import it.dbortoluzzi.domain.Booking
import it.dbortoluzzi.domain.util.ServiceResult
import it.dbortoluzzi.tuttiapposto.api.ApiHelper
import it.dbortoluzzi.tuttiapposto.api.toServiceResult
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

class AndroidBookingSource constructor(
        var apiHelper: ApiHelper
) : BookingPersistenceSource {

    override suspend fun createBooking(booking: Booking): ServiceResult<Booking> {
        return apiHelper
                .createBooking(booking)
                .toServiceResult()
    }

    override suspend fun editBooking(booking: Booking): ServiceResult<Booking> {
        return apiHelper
                .editBooking(booking)
                .toServiceResult()
    }

    override suspend fun deleteBooking(booking: Booking): ServiceResult<Boolean> {
        return apiHelper
                .deleteBooking(booking)
                .toServiceResult()
    }

    override suspend fun getBookingsBy(userId: String, companyId: String, buildingId: String?, roomId: String?, startDate: Date, endDate: Date?): ServiceResult<List<Booking>> {
        return apiHelper
                .getBookingsBy(userId, companyId, buildingId, roomId, startDate, endDate)
                .toServiceResult()
    }

    companion object {
        private val TAG = "AndroidBookingSource"
    }

}