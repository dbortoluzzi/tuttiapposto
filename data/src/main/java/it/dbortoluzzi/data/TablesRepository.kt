package it.dbortoluzzi.data

import it.dbortoluzzi.data.dto.TableAvailableRequestDto
import it.dbortoluzzi.domain.Table
import it.dbortoluzzi.domain.util.ServiceResult
import java.util.*

class TablesRepository(
    private val tablePersistenceSource: TablePersistenceSource
) {

    suspend fun findAvailableTables(tableAvailableRequestDto: TableAvailableRequestDto): ServiceResult<List<Table>> = tablePersistenceSource.getAvailableTables(tableAvailableRequestDto)

}

interface TablePersistenceSource {

    suspend fun getAvailableTables(tableAvailableRequestDto: TableAvailableRequestDto): ServiceResult<List<Table>>

}
