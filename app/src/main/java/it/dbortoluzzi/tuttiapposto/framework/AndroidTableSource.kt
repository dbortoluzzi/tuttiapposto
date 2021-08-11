package it.dbortoluzzi.tuttiapposto.framework

import it.dbortoluzzi.data.AvailabilitiesSource
import it.dbortoluzzi.domain.dto.TableAvailabilityRequestDto
import it.dbortoluzzi.domain.dto.TableAvailabilityResponseDto
import it.dbortoluzzi.domain.util.ServiceResult
import it.dbortoluzzi.tuttiapposto.api.ApiHelper
import it.dbortoluzzi.tuttiapposto.api.toServiceResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AndroidTableSource @Inject constructor(
        var apiHelper: ApiHelper
) : AvailabilitiesSource {

    override suspend fun getAvailableTables(tableAvailabilityRequestDto: TableAvailabilityRequestDto): ServiceResult<List<TableAvailabilityResponseDto>> {
        return apiHelper
                .findAvailableTable(tableAvailabilityRequestDto.companyId, tableAvailabilityRequestDto.buildingId, tableAvailabilityRequestDto.roomId, tableAvailabilityRequestDto.startDate, tableAvailabilityRequestDto.endDate, tableAvailabilityRequestDto.userId)
                .toServiceResult()

    }

    companion object {
        private val TAG = "AndroidTableSource"
    }

}