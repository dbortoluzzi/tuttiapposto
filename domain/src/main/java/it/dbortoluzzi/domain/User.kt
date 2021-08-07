package it.dbortoluzzi.domain

import java.io.Serializable

data class User(val uID: String, val email: String, val displayName: String?) : Serializable