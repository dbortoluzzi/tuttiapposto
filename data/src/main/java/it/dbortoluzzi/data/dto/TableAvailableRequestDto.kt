package it.dbortoluzzi.data.dto

import java.io.Serializable
import java.util.*

data class TableAvailableRequestDto(
        val companyId: String = "",
        val buildingId: String? = null,
        val roomId: String? = null,
        val startDate: Date = Date(),
        val endDate: Date = Date()) : Serializable