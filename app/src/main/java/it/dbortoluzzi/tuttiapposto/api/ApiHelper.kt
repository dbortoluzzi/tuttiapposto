package it.dbortoluzzi.tuttiapposto.api

import it.dbortoluzzi.domain.dto.TableAvailabilityResponseDto
import retrofit2.Response
import java.util.*

interface ApiHelper {
    suspend fun findAvailableTable(companyId: String, buildingId: String?, roomId: String?, startDate: Date, endDate: Date): Response<List<TableAvailabilityResponseDto>>
}