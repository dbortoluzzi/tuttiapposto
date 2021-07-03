package it.dbortoluzzi.domain

import java.io.Serializable

data class Table(val uID: String = "",
                 val companyId: String = "",
                 val buildingId: String = "",
                 val roomId: String = "",
                 val active: Boolean = false,
                 val name: String = "",
                 val maxCapacity: Int = 0) : Serializable