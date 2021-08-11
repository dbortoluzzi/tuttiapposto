package it.dbortoluzzi.tuttiapposto.api

import it.dbortoluzzi.domain.Booking
import it.dbortoluzzi.domain.dto.OccupationByHourResponseDto
import it.dbortoluzzi.domain.dto.OccupationByRoomResponseDto
import it.dbortoluzzi.domain.dto.TableAvailabilityResponseDto
import retrofit2.Response
import java.util.*

interface ApiHelper {
    suspend fun findAvailableTable(companyId: String, buildingId: String?, roomId: String?, startDate: Date, endDate: Date, userId: String?): Response<List<TableAvailabilityResponseDto>>

    suspend fun createBooking(booking: Booking): Response<Booking>

    suspend fun editBooking(booking: Booking): Response<Booking>

    suspend fun deleteBooking(booking: Booking): Response<Boolean>

    suspend fun getBookingsBy(userId: String, companyId: String, buildingId: String?, roomId: String?, startDate: Date, endDate: Date?): Response<List<Booking>>

    suspend fun getOccupationByHour(companyId: String, startDate: Date, endDate: Date): Response<List<OccupationByHourResponseDto>>

    suspend fun getOccupationByRoom(companyId: String, startDate: Date, endDate: Date): Response<List<OccupationByRoomResponseDto>>
}