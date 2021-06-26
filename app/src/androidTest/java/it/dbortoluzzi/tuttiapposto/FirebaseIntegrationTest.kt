package it.dbortoluzzi.tuttiapposto

import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import it.dbortoluzzi.domain.Company
import it.dbortoluzzi.domain.util.ServiceResult
import it.dbortoluzzi.tuttiapposto.framework.FirebaseCompanySource
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
    lateinit var firebaseCompanySource: FirebaseCompanySource

    @Before
    fun init() {
        hiltRule.inject();
    }

    @Test
    fun testIntegration() {
        runBlocking {
            val activeCompaniesResult: ServiceResult<List<Company>> = firebaseCompanySource.getActiveCompanies()
            Assert.assertTrue("at start company result is success", activeCompaniesResult is ServiceResult.Success)
            Assert.assertTrue("at start company is not empty", (activeCompaniesResult as ServiceResult.Success<List<Company>>).data.isNotEmpty() )
        }
    }
}
