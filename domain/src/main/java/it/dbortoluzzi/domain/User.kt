package it.dbortoluzzi.domain

import java.io.Serializable

data class User(val email: String, val displayName: String?) : Serializable