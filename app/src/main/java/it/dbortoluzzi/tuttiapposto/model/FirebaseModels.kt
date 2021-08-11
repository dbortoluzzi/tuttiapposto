package it.dbortoluzzi.tuttiapposto.model

import com.google.firebase.firestore.DocumentId
import it.dbortoluzzi.domain.*
import java.io.Serializable
import java.util.*

data class FirebaseCompany(
        @DocumentId
        val uID: String = "",
        val active: Boolean = false,
        val vatNumber: String? = null,
        val denomination: String = "",
) : Serializable

fun FirebaseCompany.toObject() = Company(this.uID, this.active, this.vatNumber, this.denomination)

data class FirebaseBuilding(
        @DocumentId
        val uID: String = "",
        val companyId: String = "",
        val active: Boolean = false,
        val name: String = "",
        val address: String? = null,
) : Serializable

fun FirebaseBuilding.toObject() = Building(this.uID, this.companyId, this.active, this.name, this.address)

data class FirebaseRoom(
        @DocumentId
        val uID: String = "",
        val companyId: String = "",
        val buildingId: String = "",
        val active: Boolean = false,
        val name: String = "",
        val description: String? = null,
) : Serializable

fun FirebaseRoom.toObject() = Room(this.uID, this.companyId, this.buildingId, this.active, this.name, this.description)

data class FirebaseTable(
        @DocumentId
        val uID: String = "",
        val companyId: String = "",
        val buildingId: String = "",
        val roomId: String = "",
        val active: Boolean = false,
        val name: String = "",
        val maxCapacity: Int = 0,
) : Serializable

fun FirebaseTable.toObject() = Table(this.uID, this.companyId, this.buildingId, this.roomId, this.active, this.name, this.maxCapacity)

data class FirebaseAvailabilityReport(
        @DocumentId
        val uID: String? = null,
        val userId: String = "",
        val companyId: String = "",
        val buildingId: String = "",
        val roomId: String = "",
        val tableId: String = "",
        val startDate: Date = Date(),
        val endDate: Date = Date(),
) : Serializable {
    constructor(uID: String?, availabilityReport: AvailabilityReport) : this(uID, availabilityReport.userId, availabilityReport.companyId, availabilityReport.buildingId, availabilityReport.roomId, availabilityReport.tableId, availabilityReport.startDate, availabilityReport.endDate)
}
fun FirebaseAvailabilityReport.toObject() = AvailabilityReport(this.uID, this.userId, this.companyId, this.buildingId, this.roomId, this.tableId, this.startDate, this.endDate)