package it.dbortoluzzi.usecases

import it.dbortoluzzi.data.RoomsRepository
import it.dbortoluzzi.domain.Room
import it.dbortoluzzi.domain.util.ServiceResult

class GetAllRooms(private val roomsRepository: RoomsRepository) {

    suspend operator fun invoke(): List<Room> {
        return when (val activeRoomsResult = roomsRepository.getActiveRooms()) {
            is ServiceResult.Success -> activeRoomsResult.data
            is ServiceResult.Error -> arrayListOf()
            else -> throw NotImplementedError()
        }
    }

}
