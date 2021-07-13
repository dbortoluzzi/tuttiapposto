package it.dbortoluzzi.tuttiapposto.model

import com.google.firebase.firestore.DocumentId
import it.dbortoluzzi.domain.Building
import it.dbortoluzzi.domain.Company
import it.dbortoluzzi.domain.Room
import java.io.Serializable

data class FirebaseCompany(
        @DocumentId
        val uID: String = "",
        val active: Boolean = false,
        val vatNumber: String? = null,
        val denomination: String = "") : Serializable

fun FirebaseCompany.toObject() = Company(this.uID, this.active, this.vatNumber, this.denomination)

data class FirebaseBuilding(
        @DocumentId
        val uID: String = "",
        val companyId: String = "",
        val active: Boolean = false,
        val name: String = "",
        val address: String? = null) : Serializable

fun FirebaseBuilding.toObject() = Building(this.uID, this.companyId, this.active, this.name, this.address)

data class FirebaseRoom(
        @DocumentId
        val uID: String = "",
        val companyId: String = "",
        val buildingId: String = "",
        val active: Boolean = false,
        val name: String = "",
        val description: String? = null) : Serializable

fun FirebaseRoom.toObject() = Room(this.uID, this.companyId, this.buildingId, this.active, this.name, this.description)

