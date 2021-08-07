package it.dbortoluzzi.tuttiapposto.api

import it.dbortoluzzi.domain.Booking
import it.dbortoluzzi.domain.dto.TableAvailabilityResponseDto
import retrofit2.Response
import java.util.*

interface ApiHelper {
    suspend fun findAvailableTable(companyId: String, buildingId: String?, roomId: String?, startDate: Date, endDate: Date): Response<List<TableAvailabilityResponseDto>>

    suspend fun createBooking(booking: Booking): Response<Booking>

    suspend fun getBookingsBy(userId: String, companyId: String, buildingId: String?, roomId: String?, startDate: Date, endDate: Date?): Response<List<Booking>>
}