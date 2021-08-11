package it.dbortoluzzi.tuttiapposto.api

import it.dbortoluzzi.domain.Booking
import it.dbortoluzzi.domain.Table
import it.dbortoluzzi.domain.dto.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService{

    @POST("/api/available/tables")
    suspend fun findAvailableTable(@Body request: TableAvailabilityRequestDto):Response<List<TableAvailabilityResponseDto>>

    @POST("/api/bookings")
    suspend fun bookTable(@Body booking: Booking):Response<Booking>

    @POST("/api/bookings/{bookingId}")
    suspend fun editBooking(@Path("bookingId") bookingId: String, @Body booking: Booking):Response<Booking>

    @DELETE("/api/bookings/{bookingId}")
    suspend fun deleteBooking(@Path("bookingId") bookingId: String):Response<Boolean>

    @POST("/api/bookings/filtered")
    suspend fun getBookingsFiltered(@Body getBookingsRequestDto: GetBookingsRequestDto):Response<List<Booking>>

    @POST("/api/statistics/occupationStatsPerHour")
    suspend fun getOccupationStatsPerHour(@Body occupationRequestDto: OccupationRequestDto):Response<List<OccupationByHourResponseDto>>

    @POST("/api/statistics/occupationStatsPerRoom")
    suspend fun getOccupationStatsPerRoom(@Body occupationRequestDto: OccupationRequestDto):Response<List<OccupationByRoomResponseDto>>
}