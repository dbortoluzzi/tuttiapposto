package it.dbortoluzzi.usecases

import it.dbortoluzzi.data.UsersRepository
import it.dbortoluzzi.domain.util.ServiceResult
import it.dbortoluzzi.domain.User

class GetUser(private val usersRepository: UsersRepository) {

    operator fun invoke(): ServiceResult<User> = usersRepository.getCurrentUser()

}
