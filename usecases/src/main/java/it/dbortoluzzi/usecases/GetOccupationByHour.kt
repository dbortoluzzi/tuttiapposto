package it.dbortoluzzi.usecases

import it.dbortoluzzi.data.StatisticsRepository
import it.dbortoluzzi.domain.dto.OccupationByHourResponseDto
import it.dbortoluzzi.domain.util.ServiceResult
import java.util.*

class GetOccupationByHour(private val statisticsRepository: StatisticsRepository) {

    suspend operator fun invoke(companyId: String, startDate: Date, endDate: Date): List<OccupationByHourResponseDto> {
        return when (val result = statisticsRepository.getOccupationByHour(companyId, startDate, endDate)) {
            is ServiceResult.Success -> result.data
            is ServiceResult.Error -> arrayListOf()
        }
    }

}
