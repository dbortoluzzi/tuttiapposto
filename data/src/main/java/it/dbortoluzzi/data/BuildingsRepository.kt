package it.dbortoluzzi.data

import it.dbortoluzzi.domain.Building
import it.dbortoluzzi.domain.Company
import it.dbortoluzzi.domain.util.ServiceResult

class BuildingsRepositoryImpl(
    private val buildingPersistenceSource: BuildingPersistenceSource
): BuildingsRepository {

    override suspend fun getActiveBuildings(): ServiceResult<List<Building>> = buildingPersistenceSource.getActiveBuildings()
    override suspend fun getActiveBuildingsByCompanyId(companyId: String): ServiceResult<List<Building>> = buildingPersistenceSource.getActiveBuildingsByCompanyId(companyId)

}

interface BuildingPersistenceSource {

    suspend fun getActiveBuildings(): ServiceResult<List<Building>>
    suspend fun getActiveBuildingsByCompanyId(companyId: String): ServiceResult<List<Building>>

}

interface BuildingsRepository {

    suspend fun getActiveBuildings(): ServiceResult<List<Building>>
    suspend fun getActiveBuildingsByCompanyId(companyId: String): ServiceResult<List<Building>>

}
