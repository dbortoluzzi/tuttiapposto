package it.dbortoluzzi.tuttiapposto.model

import com.google.firebase.firestore.DocumentId
import it.dbortoluzzi.domain.Company
import java.io.Serializable

data class FirebaseCompany(
        @DocumentId
        val uID: String = "",
        val active: Boolean = false,
        val vatNumber: String? = null,
        val denomination: String = "") : Serializable

fun FirebaseCompany.toObject() = Company(this.uID, this.active, this.vatNumber, this.denomination)

