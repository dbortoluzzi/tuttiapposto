package it.dbortoluzzi.usecases

import it.dbortoluzzi.data.TablesRepository
import it.dbortoluzzi.domain.dto.TableAvailabilityRequestDto
import it.dbortoluzzi.domain.Table
import it.dbortoluzzi.domain.dto.TableAvailabilityResponseDto
import it.dbortoluzzi.domain.util.ServiceResult
import java.util.*

class GetAvailableTables(private val tablesRepository: TablesRepository) {

    suspend operator fun invoke(companyId: String, buildingId: String?, roomId: String?, startDate: Date, endDate: Date, userId: String?): List<TableAvailabilityResponseDto> {
        return when (val serviceResult = tablesRepository.findAvailableTables(TableAvailabilityRequestDto(companyId, buildingId, roomId, startDate, endDate))) {
            is ServiceResult.Success -> serviceResult.data
            is ServiceResult.Error -> arrayListOf()
        }
    }

}
