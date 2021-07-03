package it.dbortoluzzi.domain

import java.io.Serializable

data class Room(val uID: String = "",
                val companyId: String = "",
                val buildingId: String = "",
                val active: Boolean = false,
                val name: String = "",
                val description: String? = null) : Serializable