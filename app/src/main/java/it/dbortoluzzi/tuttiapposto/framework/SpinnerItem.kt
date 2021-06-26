package it.dbortoluzzi.tuttiapposto.framework

class SpinnerItem<T>(val id: T, val text: String? = null) {
    override fun toString(): String {
        return text ?: ""
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SpinnerItem<*>

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }
}