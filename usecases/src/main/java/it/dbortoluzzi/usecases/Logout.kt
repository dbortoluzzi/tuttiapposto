package it.dbortoluzzi.usecases

import it.dbortoluzzi.data.UsersRepository
import it.dbortoluzzi.domain.util.ServiceResult
import it.dbortoluzzi.domain.User

class Logout(private val usersRepository: UsersRepository) {

    suspend operator fun invoke(): ServiceResult<Boolean> = usersRepository.logout()

}
