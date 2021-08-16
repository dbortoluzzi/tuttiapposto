package it.dbortoluzzi.data

import it.dbortoluzzi.domain.Table
import it.dbortoluzzi.domain.dto.TableAvailabilityRequestDto
import it.dbortoluzzi.domain.dto.TableAvailabilityResponseDto
import it.dbortoluzzi.domain.util.ServiceResult

class TablesRepositoryImpl(
    private val availabilitiesSource: AvailabilitiesSource,
    private val tablePersistenceSource: TablePersistenceSource
) : TablesRepository{

    override suspend fun findAvailableTables(tableAvailabilityRequestDto: TableAvailabilityRequestDto): ServiceResult<List<TableAvailabilityResponseDto>> =
            availabilitiesSource.getAvailableTables(tableAvailabilityRequestDto)
    override suspend fun getActiveTables(): ServiceResult<List<Table>> = tablePersistenceSource.getActiveTables()
    override suspend fun getActiveTablesByCompanyIdAndBuildingIdAndRoomId(companyId: String, buildingId: String, roomId: String): ServiceResult<List<Table>> =
            tablePersistenceSource.getActiveTablesByCompanyIdAndBuildingIdAndRoomId(companyId, buildingId, roomId)

}

interface AvailabilitiesSource {

    suspend fun getAvailableTables(tableAvailabilityRequestDto: TableAvailabilityRequestDto): ServiceResult<List<TableAvailabilityResponseDto>>

}

interface TablePersistenceSource {

    suspend fun getActiveTables(): ServiceResult<List<Table>>
    suspend fun getActiveTablesByCompanyIdAndBuildingIdAndRoomId(companyId: String, buildingId: String, roomId: String): ServiceResult<List<Table>>

}

interface TablesRepository {
    suspend fun findAvailableTables(tableAvailabilityRequestDto: TableAvailabilityRequestDto): ServiceResult<List<TableAvailabilityResponseDto>>;
    suspend fun getActiveTables(): ServiceResult<List<Table>>
    suspend fun getActiveTablesByCompanyIdAndBuildingIdAndRoomId(companyId: String, buildingId: String, roomId: String): ServiceResult<List<Table>>
}
