package it.dbortoluzzi.domain.dto

import it.dbortoluzzi.domain.Table
import java.io.Serializable
import java.util.*

data class TableAvailabilityResponseDto(
        val table: Table = Table(),
        val availability: Int = 0,
        val startDate: Date? = null,
        val endDate: Date? = null,
        val reported: Boolean = false,
        val alreadyReportedByUser: Boolean = false) : Serializable