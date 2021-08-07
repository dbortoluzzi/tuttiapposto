package it.dbortoluzzi.domain

import java.io.Serializable
import java.util.*

data class Booking(val uID: String? = null,
                   val userId: String = "",
                   val companyId: String = "",
                   val buildingId: String = "",
                   val roomId: String = "",
                   val tableId: String = "",
                   val startDate: Date = Date(),
                   val endDate: Date = Date()) : Serializable