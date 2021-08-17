package it.dbortoluzzi.data

import it.dbortoluzzi.domain.AvailabilityReport
import it.dbortoluzzi.domain.util.ServiceResult

class AvailabilityReportsRepositoryImpl(
    private val availabilityReportSource: AvailabilityReportSource
) : AvailabilityReportsRepository{

    override suspend fun createAvailabilityReport(availabilityReport: AvailabilityReport): ServiceResult<AvailabilityReport> = availabilityReportSource.createAvailabilityReport(availabilityReport)

}

interface AvailabilityReportSource {

    suspend fun createAvailabilityReport(availabilityReport: AvailabilityReport): ServiceResult<AvailabilityReport>

}

interface AvailabilityReportsRepository {

    suspend fun createAvailabilityReport(availabilityReport: AvailabilityReport): ServiceResult<AvailabilityReport>

}
