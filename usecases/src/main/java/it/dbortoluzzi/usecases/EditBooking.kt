package it.dbortoluzzi.usecases

import it.dbortoluzzi.data.BookingsRepository
import it.dbortoluzzi.domain.Booking
import it.dbortoluzzi.domain.util.ServiceResult

class EditBooking(private val bookingsRepository: BookingsRepository) {

    suspend operator fun invoke(booking: Booking): Booking? {
        return when (val result = bookingsRepository.editBooking(booking)) {
            is ServiceResult.Success -> result.data
            is ServiceResult.Error -> null
        }
    }

}
