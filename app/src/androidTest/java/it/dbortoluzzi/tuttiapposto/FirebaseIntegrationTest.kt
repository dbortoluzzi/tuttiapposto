package it.dbortoluzzi.tuttiapposto

import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import it.dbortoluzzi.data.*
import it.dbortoluzzi.domain.Building
import it.dbortoluzzi.domain.Company
import it.dbortoluzzi.domain.Room
import it.dbortoluzzi.domain.Table
import it.dbortoluzzi.domain.util.ServiceResult
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject


@HiltAndroidTest
class FirebaseIntegrationTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var firebaseCompanyPersistenceSource: CompanyPersistenceSource

    @Inject
    lateinit var firebaseBuildingPersistenceSource: BuildingPersistenceSource

    @Inject
    lateinit var firebaseRoomPersistenceSource: RoomPersistenceSource

    @Inject
    lateinit var firebaseTablePersistenceSource: TablePersistenceSource

    @Inject
    lateinit var bookingPersistenceSource: BookingPersistenceSource

    @Before
    fun init() {
        hiltRule.inject();
    }

    @Test
    fun testFirebaseIntegration() {
        runBlocking {
            // companies
            val activeCompaniesResult: ServiceResult<List<Company>> = firebaseCompanyPersistenceSource.getActiveCompanies()
            Assert.assertTrue("at start company result is success", activeCompaniesResult is ServiceResult.Success)
            Assert.assertTrue("at start company is not empty", (activeCompaniesResult as ServiceResult.Success<List<Company>>).data.isNotEmpty() )
            val company = activeCompaniesResult.data.first();

            // buildings
            val activeBuildingsResult: ServiceResult<List<Building>> = firebaseBuildingPersistenceSource.getActiveBuildingsByCompanyId(company.uID)
            Assert.assertTrue("at start building result is success", activeBuildingsResult is ServiceResult.Success)
            Assert.assertTrue("at start building is not empty", (activeBuildingsResult as ServiceResult.Success<List<Building>>).data.isNotEmpty() )

            // availabilities
            val activeRoomsResult: ServiceResult<List<Room>> = firebaseRoomPersistenceSource.getActiveRoomsByCompany(company.uID)
            Assert.assertTrue("at start rooms result is success", activeRoomsResult is ServiceResult.Success)
            Assert.assertTrue("at start rooms is not empty", (activeRoomsResult as ServiceResult.Success<List<Room>>).data.isNotEmpty() )
            val room = activeRoomsResult.data.first()

            // tables
            val activeTablesResult: ServiceResult<List<Table>> = firebaseTablePersistenceSource.getActiveTablesByCompanyIdAndBuildingIdAndRoomId(room.companyId, room.buildingId, room.uID)
            Assert.assertTrue("at start tables result is success", activeTablesResult is ServiceResult.Success)
            Assert.assertTrue("at start tables is not empty", (activeTablesResult as ServiceResult.Success<List<Table>>).data.isNotEmpty() )
        }
    }
}
