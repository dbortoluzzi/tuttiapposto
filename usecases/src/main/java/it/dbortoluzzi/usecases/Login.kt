package it.dbortoluzzi.usecases

import it.dbortoluzzi.data.UsersRepository
import it.dbortoluzzi.domain.util.ServiceResult
import it.dbortoluzzi.domain.User

class Login(private val usersRepository: UsersRepository) {

    suspend operator fun invoke(mail: String, password: String): ServiceResult<User> = usersRepository.login(mail, password)

}
