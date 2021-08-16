package it.dbortoluzzi.tuttiapposto

import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import it.dbortoluzzi.data.AvailabilitiesSource
import it.dbortoluzzi.data.BookingsRepository
import it.dbortoluzzi.data.StatisticsPersistenceSource
import it.dbortoluzzi.domain.Booking
import it.dbortoluzzi.domain.Company
import it.dbortoluzzi.domain.dto.OccupationByHourResponseDto
import it.dbortoluzzi.domain.dto.OccupationByRoomResponseDto
import it.dbortoluzzi.domain.dto.TableAvailabilityRequestDto
import it.dbortoluzzi.domain.dto.TableAvailabilityResponseDto
import it.dbortoluzzi.domain.util.ServiceResult
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*
import javax.inject.Inject


@HiltAndroidTest
class RetrofitIntegrationTest {

    private val USER_ID_TEST = "Feyitm6eaoVmCqggutdRjhY2AKB3"

    private val COMPANY_ID_TEST = "FbF0or0c0NdBphbZcssm"

    private val BUILDING_ID_TEST = "VTdqvUGCKLWKq0SFkTHx"

    private val ROOM_ID_TEST = "B29tSJlDqC6J6OG9Jcug"

    private val TABLE_ID_TEST = "hepxdHgG9Zejol2BWw0F"

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var androidBookingsRepository: BookingsRepository

    @Inject
    lateinit var availabilitiesSource: AvailabilitiesSource

    @Inject
    lateinit var statisticsPersistenceSource: StatisticsPersistenceSource

    @Before
    fun init() {
        hiltRule.inject();
    }

    @Test
    fun testRetrofitIntegration() {
        runBlocking {
            // availabilities
            val availableTablesResult: ServiceResult<List<TableAvailabilityResponseDto>> = availabilitiesSource.getAvailableTables(
                TableAvailabilityRequestDto(
                    COMPANY_ID_TEST,
                    null,
                    null,
                    Date(),
                    Date()
                )
            )
            Assert.assertTrue("at start availabilities result is success", availableTablesResult is ServiceResult.Success)
            Assert.assertTrue("at start availabilities is not empty", (availableTablesResult as ServiceResult.Success<List<TableAvailabilityResponseDto>>).data.isNotEmpty() )

            // statistics
            val occupationByHourResult: ServiceResult<List<OccupationByHourResponseDto>> = statisticsPersistenceSource.getOccupationByHour(COMPANY_ID_TEST, Date(), Date())
            Assert.assertTrue("at start statistics.occupationByHour result is success", occupationByHourResult is ServiceResult.Success)
            val occupationByRoomResult: ServiceResult<List<OccupationByRoomResponseDto>> = statisticsPersistenceSource.getOccupationByRoom(COMPANY_ID_TEST, Date(), Date())
            Assert.assertTrue("at start statistics.occupationByRoom result is success", occupationByRoomResult is ServiceResult.Success)
            
            val dateCal = Calendar.getInstance()
            dateCal.set(Calendar.DAY_OF_YEAR, 1)
            val bookingsResult: ServiceResult<List<Booking>> = androidBookingsRepository.getBookingsBy(USER_ID_TEST, COMPANY_ID_TEST, null, null, dateCal.time, null)
            Assert.assertTrue("at start booking result is success", bookingsResult is ServiceResult.Success)
            Assert.assertTrue("at start booking is not empty", (bookingsResult as ServiceResult.Success<List<Company>>).data.isNotEmpty())

            // create booking
            val startDateCal = Calendar.getInstance()
            startDateCal.set(Calendar.HOUR_OF_DAY, startDateCal.get(Calendar.HOUR_OF_DAY)+1)
            val endDateCal = startDateCal.clone() as Calendar
            endDateCal.set(Calendar.HOUR_OF_DAY, startDateCal.get(Calendar.HOUR_OF_DAY)+1)
            val createBookingResult: ServiceResult<Booking> = androidBookingsRepository.createBooking(
                Booking(
                    null,
                    USER_ID_TEST,
                    COMPANY_ID_TEST,
                    BUILDING_ID_TEST,
                    ROOM_ID_TEST,
                    TABLE_ID_TEST,
                    startDateCal.time,
                    endDateCal.time
                )
            )
            Assert.assertTrue("create booking result is success", createBookingResult is ServiceResult.Success)
            Assert.assertTrue("create booking is not empty", (createBookingResult as ServiceResult.Success<Booking>).data.uID != null)
            // delete booking
            val deleteBookingResult: ServiceResult<Boolean> = androidBookingsRepository.deleteBooking(createBookingResult.data)
            Assert.assertTrue("delete booking result is success", deleteBookingResult is ServiceResult.Success)
            Assert.assertTrue("delete booking is not empty", (deleteBookingResult as ServiceResult.Success<Boolean>).data)
        }
    }
}
