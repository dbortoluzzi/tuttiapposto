package it.dbortoluzzi.usecases

import it.dbortoluzzi.data.RoomsRepository
import it.dbortoluzzi.domain.Room
import it.dbortoluzzi.domain.util.ServiceResult

class GetRooms(private val roomsRepository: RoomsRepository) {

    suspend operator fun invoke(companyId: String, buildingId: String): List<Room> {
        return when (val activeRoomsResult = roomsRepository.getActiveRoomsByCompanyIdAndBuildingId(companyId, buildingId)) {
            is ServiceResult.Success -> activeRoomsResult.data
            is ServiceResult.Error -> arrayListOf()
        }
    }

}
