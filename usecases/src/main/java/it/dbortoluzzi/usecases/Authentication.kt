package it.dbortoluzzi.usecases

import it.dbortoluzzi.data.UsersRepository
import it.dbortoluzzi.domain.User
import it.dbortoluzzi.domain.util.ServiceResult

class GetUser(private val usersRepository: UsersRepository) {

    operator fun invoke(): ServiceResult<User> = usersRepository.getCurrentUser()

}

class Login(private val usersRepository: UsersRepository) {

    suspend operator fun invoke(mail: String, password: String): ServiceResult<User> = usersRepository.login(mail, password)

}

class Logout(private val usersRepository: UsersRepository) {

    suspend operator fun invoke(): ServiceResult<Boolean> = usersRepository.logout()

}

class Register(private val usersRepository: UsersRepository) {

    suspend operator fun invoke(mail: String, password: String): ServiceResult<User> = usersRepository.register(mail, password)

}
