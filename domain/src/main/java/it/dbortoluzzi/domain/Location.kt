package it.dbortoluzzi.domain

import java.io.Serializable
import java.util.*

data class Location(val latitude: Double, val longitude: Double, val date: Date) : Serializable