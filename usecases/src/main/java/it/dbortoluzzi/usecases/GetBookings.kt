package it.dbortoluzzi.usecases

import it.dbortoluzzi.data.BookingsRepository
import it.dbortoluzzi.domain.Booking
import it.dbortoluzzi.domain.util.ServiceResult
import java.util.*

class GetBookings(private val bookingsRepository: BookingsRepository) {

    suspend operator fun invoke(userId: String, companyId: String, buildingId: String?, roomId: String?, startDate: Date, endDate: Date?): List<Booking> {
        return when (val bookingsResult = bookingsRepository.getBookingsBy(userId, companyId, buildingId, roomId, startDate, endDate)) {
            is ServiceResult.Success -> bookingsResult.data
            is ServiceResult.Error -> arrayListOf()
        }
    }

}
