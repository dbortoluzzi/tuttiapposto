package it.dbortoluzzi.domain.dto

import java.io.Serializable
import java.util.*

data class OccupationByHourResponseDto(
        val date: Date = Date(),
        val occupation: Long = 0) : Serializable