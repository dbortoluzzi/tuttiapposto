package it.dbortoluzzi.tuttiapposto

import it.dbortoluzzi.data.TablesRepository
import it.dbortoluzzi.domain.Table
import it.dbortoluzzi.domain.dto.TableAvailabilityResponseDto
import it.dbortoluzzi.domain.util.ServiceResult
import it.dbortoluzzi.usecases.GetAvailableTables
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.kotlin.*
import java.util.*

class UseCasesModuleTest {

    @Test
    fun getAvailableTables_positive_test() = runBlocking {
        val fixture: TablesRepository = mock()

        given(fixture.findAvailableTables(any())).willSuspendableAnswer {
            withContext(Dispatchers.Default) { ServiceResult.Success(listOf(TableAvailabilityResponseDto(
                Table(),
                10,
                Date(),
                Date(),
                false,
                false
            ))) }
        }

        val invoked = GetAvailableTables(fixture).invoke(
            "COMPANY_ID",
            "BUILDING_ID",
            "ROOM_ID",
            Date(),
            Date(),
            null
        )
        assertTrue(invoked.size == 1)
    }

    @Test
    fun getAvailableTables_negative_test() = runBlocking {
        val fixture: TablesRepository = mock()

        given(fixture.findAvailableTables(any())).willSuspendableAnswer {
            withContext(Dispatchers.Default) { ServiceResult.Error("GENERIC_ERROR") }
        }

        val invoked = GetAvailableTables(fixture).invoke(
            "COMPANY_ID",
            "BUILDING_ID",
            "ROOM_ID",
            Date(),
            Date(),
            null
        )
        assertTrue(invoked.isEmpty())
    }

}
