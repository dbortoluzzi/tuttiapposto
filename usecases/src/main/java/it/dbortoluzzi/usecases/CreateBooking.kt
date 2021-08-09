package it.dbortoluzzi.usecases

import it.dbortoluzzi.data.BookingsRepository
import it.dbortoluzzi.domain.Booking
import it.dbortoluzzi.domain.util.ServiceResult

class CreateBooking(private val bookingsRepository: BookingsRepository) {

    suspend operator fun invoke(booking: Booking): Booking? {
        return when (val activeBuildingsResult = bookingsRepository.createBooking(booking)) {
            is ServiceResult.Success -> activeBuildingsResult.data
            is ServiceResult.Error -> null
        }
    }

}
