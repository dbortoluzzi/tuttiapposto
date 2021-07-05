package it.dbortoluzzi.tuttiapposto.api

import it.dbortoluzzi.domain.dto.TableAvailabilityRequestDto
import it.dbortoluzzi.domain.Table
import it.dbortoluzzi.domain.dto.TableAvailabilityResponseDto
import it.dbortoluzzi.tuttiapposto.annotation.Cacheable
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService{

    @POST("/api/available/tables")
    @Cacheable
    suspend fun findAvailableTable(@Body request: TableAvailabilityRequestDto):Response<List<TableAvailabilityResponseDto>>
}