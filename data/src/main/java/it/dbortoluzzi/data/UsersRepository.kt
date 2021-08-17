package it.dbortoluzzi.data

import it.dbortoluzzi.domain.User
import it.dbortoluzzi.domain.util.ServiceResult

class UsersRepositoryImpl(
        private val authenticationSource: AuthenticationSource
): UsersRepository {

    override suspend fun login(mail: String, password: String): ServiceResult<User> {
        val result = authenticationSource.login(mail, password)
        return when (result) {
            is ServiceResult.Success -> {
                authenticationSource.getCurrentUser()
            }
            is ServiceResult.Error -> ServiceResult.Error(result.exception)
        }
    }

    override suspend fun register(mail: String, password: String): ServiceResult<User> {
        val result = authenticationSource.register(mail, password)
        return when (result) {
            is ServiceResult.Success -> {
                authenticationSource.getCurrentUser()
            }
            is ServiceResult.Error -> ServiceResult.Error(result.exception)
        }
    }

    override suspend fun logout(): ServiceResult<Boolean> {
        return authenticationSource.logout()
    }

    override fun getCurrentUser(): ServiceResult<User> {
        return authenticationSource.getCurrentUser()
    }
}

interface AuthenticationSource {

    suspend fun login(mail: String, password: String): ServiceResult<String>
    suspend fun register(mail: String, password: String): ServiceResult<String>
    suspend fun logout(): ServiceResult<Boolean>
    fun getCurrentUser(): ServiceResult<User>
}

interface UsersRepository {

    suspend fun login(mail: String, password: String): ServiceResult<User>
    suspend fun register(mail: String, password: String): ServiceResult<User>
    suspend fun logout(): ServiceResult<Boolean>
    fun getCurrentUser(): ServiceResult<User>
}
