package it.dbortoluzzi.data

import it.dbortoluzzi.domain.Room
import it.dbortoluzzi.domain.util.ServiceResult

class RoomsRepository(
    private val roomPersistenceSource: RoomPersistenceSource
) {

    suspend fun getActiveRooms(): ServiceResult<List<Room>> = roomPersistenceSource.getActiveRooms()
    suspend fun getActiveRoomsByCompanyIdAndBuildingId(companyId: String, buildingId: String): ServiceResult<List<Room>> = roomPersistenceSource.getActiveRoomsByCompanyIdAndBuildingId(companyId, buildingId)

}

interface RoomPersistenceSource {

    suspend fun getActiveRooms(): ServiceResult<List<Room>>
    suspend fun getActiveRoomsByCompanyIdAndBuildingId(companyId: String, buildingId: String): ServiceResult<List<Room>>

}
