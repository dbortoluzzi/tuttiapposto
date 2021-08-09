package it.dbortoluzzi.usecases

import it.dbortoluzzi.data.BookingsRepository
import it.dbortoluzzi.domain.Booking
import it.dbortoluzzi.domain.util.ServiceResult

class DeleteBooking(private val bookingsRepository: BookingsRepository) {

    suspend operator fun invoke(booking: Booking): Boolean {
        return when (val result = bookingsRepository.deleteBooking(booking)) {
            is ServiceResult.Success -> result.data
            is ServiceResult.Error -> false
        }
    }

}
