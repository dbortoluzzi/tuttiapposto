package it.dbortoluzzi.tuttiapposto.api

import it.dbortoluzzi.data.dto.TableAvailableRequestDto
import it.dbortoluzzi.domain.Table
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService{

    @POST("/api/tables/available")
    suspend fun findAvailableTable(@Body request: TableAvailableRequestDto):Response<List<Table>>
}