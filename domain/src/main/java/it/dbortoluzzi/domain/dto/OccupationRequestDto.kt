package it.dbortoluzzi.domain.dto

import java.io.Serializable
import java.util.*

data class OccupationRequestDto(
        val companyId: String = "",
        val startDate: Date = Date(),
        val endDate: Date = Date()) : Serializable