package it.dbortoluzzi.usecases

import it.dbortoluzzi.data.TablesRepository
import it.dbortoluzzi.domain.Table
import it.dbortoluzzi.domain.util.ServiceResult

class GetTablesWithFilters(private val tablesRepository: TablesRepository) {

    suspend operator fun invoke(companyId: String, buildingId: String, roomId: String): List<Table> {
        return when (val activeTablesResult = tablesRepository.getActiveTablesByCompanyIdAndBuildingIdAndRoomId(companyId, buildingId, roomId)) {
            is ServiceResult.Success -> activeTablesResult.data
            is ServiceResult.Error -> arrayListOf()
        }
    }

}
