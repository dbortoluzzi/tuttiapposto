package it.dbortoluzzi.data

import it.dbortoluzzi.domain.AvailabilityReport
import it.dbortoluzzi.domain.util.ServiceResult

class AvailabilityReportsRepository(
    private val availabilityReportSource: AvailabilityReportSource
) {

    suspend fun createAvailabilityReport(availabilityReport: AvailabilityReport): ServiceResult<AvailabilityReport> = availabilityReportSource.createAvailabilityReport(availabilityReport)

}

interface AvailabilityReportSource {

    suspend fun createAvailabilityReport(availabilityReport: AvailabilityReport): ServiceResult<AvailabilityReport>

}
