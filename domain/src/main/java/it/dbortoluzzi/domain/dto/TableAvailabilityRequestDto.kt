package it.dbortoluzzi.domain.dto

import java.io.Serializable
import java.util.*

data class TableAvailabilityRequestDto(
        val companyId: String = "",
        val buildingId: String? = null,
        val roomId: String? = null,
        val startDate: Date = Date(),
        val endDate: Date = Date()) : Serializable