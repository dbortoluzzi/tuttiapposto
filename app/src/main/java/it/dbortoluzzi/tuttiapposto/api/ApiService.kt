package it.dbortoluzzi.tuttiapposto.api

import it.dbortoluzzi.domain.Booking
import it.dbortoluzzi.domain.dto.TableAvailabilityRequestDto
import it.dbortoluzzi.domain.Table
import it.dbortoluzzi.domain.dto.TableAvailabilityResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService{

    @POST("/api/available/tables")
    suspend fun findAvailableTable(@Body request: TableAvailabilityRequestDto):Response<List<TableAvailabilityResponseDto>>

    @POST("/api/bookings")
    suspend fun bookTable(@Body booking: Booking):Response<Booking>
}