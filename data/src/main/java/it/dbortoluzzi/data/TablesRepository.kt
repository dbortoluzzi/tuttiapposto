package it.dbortoluzzi.data

import it.dbortoluzzi.domain.Table
import it.dbortoluzzi.domain.util.ServiceResult

class TablesRepository(
    private val tablePersistenceSource: TablePersistenceSource
) {

    suspend fun findAvailableTables(): ServiceResult<List<Table>> = tablePersistenceSource.getAvailableTables()

}

interface TablePersistenceSource {

    suspend fun getAvailableTables(): ServiceResult<List<Table>>

}
