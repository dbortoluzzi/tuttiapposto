package it.dbortoluzzi.data

import it.dbortoluzzi.domain.dto.TableAvailabilityRequestDto
import it.dbortoluzzi.domain.Table
import it.dbortoluzzi.domain.dto.TableAvailabilityResponseDto
import it.dbortoluzzi.domain.util.ServiceResult

class TablesRepository(
    private val tablePersistenceSource: TablePersistenceSource
) {

    suspend fun findAvailableTables(tableAvailabilityRequestDto: TableAvailabilityRequestDto): ServiceResult<List<TableAvailabilityResponseDto>> = tablePersistenceSource.getAvailableTables(tableAvailabilityRequestDto)

}

interface TablePersistenceSource {

    suspend fun getAvailableTables(tableAvailabilityRequestDto: TableAvailabilityRequestDto): ServiceResult<List<TableAvailabilityResponseDto>>

}
