package it.dbortoluzzi.domain.dto

import java.io.Serializable
import java.util.*

data class OccupationByRoomResponseDto(
        val elementId: String = "",
        val occupation: Long = 0) : Serializable