package it.dbortoluzzi.data

import it.dbortoluzzi.domain.Booking
import it.dbortoluzzi.domain.util.ServiceResult
import java.util.*

class BookingsRepository(
    private val bookingPersistenceSource: BookingPersistenceSource
) {

    suspend fun createBooking(booking: Booking): ServiceResult<Booking> = bookingPersistenceSource.createBooking(booking)
    suspend fun deleteBooking(booking: Booking): ServiceResult<Boolean> = bookingPersistenceSource.deleteBooking(booking)
    suspend fun getBookingsBy(userId: String, companyId: String, buildingId: String?, roomId: String?, startDate: Date, endDate: Date?): ServiceResult<List<Booking>> = bookingPersistenceSource.getBookingsBy(userId, companyId, buildingId, roomId, startDate, endDate)
}

interface BookingPersistenceSource {

    suspend fun createBooking(booking: Booking): ServiceResult<Booking>
    suspend fun deleteBooking(booking: Booking): ServiceResult<Boolean>
    suspend fun getBookingsBy(userId: String, companyId: String, buildingId: String?, roomId: String?, startDate: Date, endDate: Date?): ServiceResult<List<Booking>>
}
