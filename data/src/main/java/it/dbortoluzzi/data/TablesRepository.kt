package it.dbortoluzzi.data

import it.dbortoluzzi.domain.Table
import it.dbortoluzzi.domain.dto.TableAvailabilityRequestDto
import it.dbortoluzzi.domain.dto.TableAvailabilityResponseDto
import it.dbortoluzzi.domain.util.ServiceResult

class TablesRepository(
    private val availabilitiesSource: AvailabilitiesSource,
    private val tablePersistenceSource: TablePersistenceSource
) {

    suspend fun findAvailableTables(tableAvailabilityRequestDto: TableAvailabilityRequestDto): ServiceResult<List<TableAvailabilityResponseDto>> =
            availabilitiesSource.getAvailableTables(tableAvailabilityRequestDto)
    suspend fun getActiveTables(): ServiceResult<List<Table>> = tablePersistenceSource.getActiveTables()
    suspend fun getActiveTablesByCompanyIdAndBuildingIdAndRoomId(companyId: String, buildingId: String, roomId: String): ServiceResult<List<Table>> =
            tablePersistenceSource.getActiveTablesByCompanyIdAndBuildingIdAndRoomId(companyId, buildingId, roomId)

}

interface AvailabilitiesSource {

    suspend fun getAvailableTables(tableAvailabilityRequestDto: TableAvailabilityRequestDto): ServiceResult<List<TableAvailabilityResponseDto>>

}

interface TablePersistenceSource {

    suspend fun getActiveTables(): ServiceResult<List<Table>>
    suspend fun getActiveTablesByCompanyIdAndBuildingIdAndRoomId(companyId: String, buildingId: String, roomId: String): ServiceResult<List<Table>>

}
