package it.dbortoluzzi.usecases

import it.dbortoluzzi.data.AvailabilityReportsRepository
import it.dbortoluzzi.domain.AvailabilityReport
import it.dbortoluzzi.domain.util.ServiceResult

class CreateAvailabilityReport(private val availabilityReportsRepository: AvailabilityReportsRepository) {

    suspend operator fun invoke(availabilityReport: AvailabilityReport): AvailabilityReport? {
        return when (val activeBuildingsResult = availabilityReportsRepository.createAvailabilityReport(availabilityReport)) {
            is ServiceResult.Success -> activeBuildingsResult.data
            is ServiceResult.Error -> null
        }
    }

}
