package it.dbortoluzzi.data

import it.dbortoluzzi.domain.dto.OccupationByHourResponseDto
import it.dbortoluzzi.domain.dto.OccupationByRoomResponseDto
import it.dbortoluzzi.domain.util.ServiceResult
import java.util.*

class StatisticsRepository(
    private val statisticsPersistenceSource: StatisticsPersistenceSource
) {

    suspend fun getOccupationByHour(companyId: String, startDate: Date, endDate: Date): ServiceResult<List<OccupationByHourResponseDto>> = statisticsPersistenceSource.getOccupationByHour(companyId, startDate, endDate)
    suspend fun getOccupationByRoom(companyId: String, startDate: Date, endDate: Date): ServiceResult<List<OccupationByRoomResponseDto>> = statisticsPersistenceSource.getOccupationByRoom(companyId, startDate, endDate)
}

interface StatisticsPersistenceSource {

    suspend fun getOccupationByHour(companyId: String, startDate: Date, endDate: Date): ServiceResult<List<OccupationByHourResponseDto>>
    suspend fun getOccupationByRoom(companyId: String, startDate: Date, endDate: Date): ServiceResult<List<OccupationByRoomResponseDto>>
}
