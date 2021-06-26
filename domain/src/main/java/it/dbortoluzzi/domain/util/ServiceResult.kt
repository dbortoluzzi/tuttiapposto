package it.dbortoluzzi.domain.util

sealed class ServiceResult<out T : Any>
{
    data class Success<out T : Any>(val data: T) : ServiceResult<T>() // Status success and data of the result
    data class Error(val exception: Exception) : ServiceResult<Nothing>() {
        constructor(message: String): this(java.lang.Exception(message))
    } // Status Error an error message

    // string method to display a result for debugging
    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
        }
    }
}