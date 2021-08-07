package it.dbortoluzzi.tuttiapposto

import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import it.dbortoluzzi.data.BookingsRepository
import it.dbortoluzzi.domain.Booking
import it.dbortoluzzi.domain.Company
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

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var androidBookingsRepository: BookingsRepository

    @Before
    fun init() {
        hiltRule.inject();
    }

    @Test
    fun testIntegration() {
        runBlocking {
            val bookingsResult: ServiceResult<List<Booking>> = androidBookingsRepository.getBookingsBy(USER_ID_TEST, COMPANY_ID_TEST, null, null, Date(), null)
            Assert.assertTrue("at start booking result is success", bookingsResult is ServiceResult.Success)
            Assert.assertTrue("at start booking is not empty", (bookingsResult as ServiceResult.Success<List<Company>>).data.isNotEmpty())
        }
    }
}
