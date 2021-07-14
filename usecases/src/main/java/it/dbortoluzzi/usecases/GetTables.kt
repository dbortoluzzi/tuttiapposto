package it.dbortoluzzi.usecases

import it.dbortoluzzi.data.TablesRepository
import it.dbortoluzzi.domain.Table
import it.dbortoluzzi.domain.util.ServiceResult

class GetTables(private val tablesRepository: TablesRepository) {

    suspend operator fun invoke(): List<Table> {
        return when (val activeTablesResult = tablesRepository.getActiveTables()) {
            is ServiceResult.Success -> activeTablesResult.data
            is ServiceResult.Error -> arrayListOf()
            else -> throw NotImplementedError()
        }
    }

}
