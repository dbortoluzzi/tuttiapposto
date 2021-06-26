package it.dbortoluzzi.data

import it.dbortoluzzi.domain.User
import it.dbortoluzzi.domain.util.ServiceResult

class UsersRepository(
        private val authenticationSource: AuthenticationSource
) {

    suspend fun login(mail: String, password: String): ServiceResult<User> {
        val result = authenticationSource.login(mail, password)
        return when (result) {
            is ServiceResult.Success -> {
                authenticationSource.getCurrentUser()
            }
            is ServiceResult.Error -> ServiceResult.Error(result.exception)
        }
    }

    suspend fun register(mail: String, password: String): ServiceResult<User> {
        val result = authenticationSource.register(mail, password)
        return when (result) {
            is ServiceResult.Success -> {
                authenticationSource.getCurrentUser()
            }
            is ServiceResult.Error -> ServiceResult.Error(result.exception)
        }
    }

    suspend fun logout(): ServiceResult<Boolean> {
        return authenticationSource.logout()
    }

    fun getCurrentUser(): ServiceResult<User> {
        return authenticationSource.getCurrentUser()
    }
}

interface AuthenticationSource {

    suspend fun login(mail: String, password: String): ServiceResult<String>
    suspend fun register(mail: String, password: String): ServiceResult<String>
    suspend fun logout(): ServiceResult<Boolean>
    fun getCurrentUser(): ServiceResult<User>
}