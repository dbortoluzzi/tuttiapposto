package it.dbortoluzzi.usecases

import it.dbortoluzzi.data.StatisticsRepository
import it.dbortoluzzi.domain.dto.OccupationByRoomResponseDto
import it.dbortoluzzi.domain.util.ServiceResult
import java.util.*

class GetOccupationByRoom(private val statisticsRepository: StatisticsRepository) {

    suspend operator fun invoke(companyId: String, startDate: Date, endDate: Date): List<OccupationByRoomResponseDto> {
        return when (val result = statisticsRepository.getOccupationByRoom(companyId, startDate, endDate)) {
            is ServiceResult.Success -> result.data
            is ServiceResult.Error -> arrayListOf()
        }
    }

}
