package it.dbortoluzzi.data

import it.dbortoluzzi.domain.Room
import it.dbortoluzzi.domain.util.ServiceResult

class RoomsRepositoryImpl(
    private val roomPersistenceSource: RoomPersistenceSource
): RoomsRepository {

    override suspend fun getActiveRooms(): ServiceResult<List<Room>> = roomPersistenceSource.getActiveRooms()
    override suspend fun getActiveRoomsByCompany(companyId: String): ServiceResult<List<Room>> = roomPersistenceSource.getActiveRoomsByCompany(companyId)
    override suspend fun getActiveRoomsByCompanyIdAndBuildingId(companyId: String, buildingId: String): ServiceResult<List<Room>> = roomPersistenceSource.getActiveRoomsByCompanyIdAndBuildingId(companyId, buildingId)

}

interface RoomPersistenceSource {

    suspend fun getActiveRooms(): ServiceResult<List<Room>>
    suspend fun getActiveRoomsByCompany(companyId: String): ServiceResult<List<Room>>
    suspend fun getActiveRoomsByCompanyIdAndBuildingId(companyId: String, buildingId: String): ServiceResult<List<Room>>

}

interface RoomsRepository {

    suspend fun getActiveRooms(): ServiceResult<List<Room>>
    suspend fun getActiveRoomsByCompany(companyId: String): ServiceResult<List<Room>>
    suspend fun getActiveRoomsByCompanyIdAndBuildingId(companyId: String, buildingId: String): ServiceResult<List<Room>>

}
