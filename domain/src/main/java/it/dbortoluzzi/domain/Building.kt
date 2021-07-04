package it.dbortoluzzi.domain

import java.io.Serializable

data class Building(val uID: String = "",
                    val companyId: String = "",
                    val active: Boolean = false,
                    val name: String = "",
                    val address: String? = null) : Serializable