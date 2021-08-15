package it.dbortoluzzi.tuttiapposto.framework

import it.dbortoluzzi.data.StatisticsPersistenceSource
import it.dbortoluzzi.domain.dto.OccupationByHourResponseDto
import it.dbortoluzzi.domain.dto.OccupationByRoomResponseDto
import it.dbortoluzzi.domain.util.ServiceResult
import it.dbortoluzzi.tuttiapposto.api.ApiHelper
import it.dbortoluzzi.tuttiapposto.api.toServiceResult
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

class AndroidStatisticsSource constructor(
        var apiHelper: ApiHelper
) : StatisticsPersistenceSource {

    override suspend fun getOccupationByHour(companyId: String, startDate: Date, endDate: Date): ServiceResult<List<OccupationByHourResponseDto>> {
        return apiHelper
                .getOccupationByHour(companyId, startDate, endDate)
                .toServiceResult()
    }

    override suspend fun getOccupationByRoom(companyId: String, startDate: Date, endDate: Date): ServiceResult<List<OccupationByRoomResponseDto>> {
        return apiHelper
                .getOccupationByRoom(companyId, startDate, endDate)
                .toServiceResult()
    }

    companion object {
        private val TAG = "AndroidStatisticsSource"
    }

}