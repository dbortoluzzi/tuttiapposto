package it.dbortoluzzi.usecases

import it.dbortoluzzi.data.TablesRepository
import it.dbortoluzzi.data.dto.TableAvailableRequestDto
import it.dbortoluzzi.domain.Table
import it.dbortoluzzi.domain.util.ServiceResult
import java.util.*

class GetAvailableTables(private val tablesRepository: TablesRepository) {

    suspend operator fun invoke(companyId: String, buildingId: String?, roomId: String?, startDate: Date, endDate: Date): List<Table> {
        return when (val serviceResult = tablesRepository.findAvailableTables(TableAvailableRequestDto(companyId, buildingId, roomId, startDate, endDate))) {
            is ServiceResult.Success -> serviceResult.data
            is ServiceResult.Error -> arrayListOf()
            else -> throw NotImplementedError()
        }
    }

}
