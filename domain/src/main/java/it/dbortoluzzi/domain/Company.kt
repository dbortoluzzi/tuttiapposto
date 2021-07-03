package it.dbortoluzzi.domain

import java.io.Serializable
import java.util.*

data class Company(val uID: String = "",
                   val active: Boolean = false,
                   val vatNumber: String? = null,
                   val denomination: String = "") : Serializable