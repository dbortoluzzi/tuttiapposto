package it.dbortoluzzi.data

import it.dbortoluzzi.domain.Booking
import it.dbortoluzzi.domain.util.ServiceResult

class BookingsRepository(
    private val bookingPersistenceSource: BookingPersistenceSource
) {

    suspend fun createBooking(booking: Booking): ServiceResult<Booking> = bookingPersistenceSource.createBooking(booking)

}

interface BookingPersistenceSource {

    suspend fun createBooking(booking: Booking): ServiceResult<Booking>

}
