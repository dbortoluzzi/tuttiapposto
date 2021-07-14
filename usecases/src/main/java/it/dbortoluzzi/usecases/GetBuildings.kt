package it.dbortoluzzi.usecases

import it.dbortoluzzi.data.BuildingsRepository
import it.dbortoluzzi.domain.Building
import it.dbortoluzzi.domain.util.ServiceResult

class GetBuildings(private val buildingsRepository: BuildingsRepository) {

    suspend operator fun invoke(companyId: String): List<Building> {
        return when (val activeBuildingsResult = buildingsRepository.getActiveBuildingsByCompanyId(companyId)) {
            is ServiceResult.Success -> activeBuildingsResult.data
            is ServiceResult.Error -> arrayListOf()
            else -> throw NotImplementedError()
        }
    }

}
