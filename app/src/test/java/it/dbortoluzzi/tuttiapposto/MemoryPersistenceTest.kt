package it.dbortoluzzi.tuttiapposto

import it.dbortoluzzi.domain.Location
import it.dbortoluzzi.tuttiapposto.framework.InMemoryLocationPersistenceSource
import org.junit.Test

import org.junit.Assert.*
import java.util.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class MemoryPersistenceTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun `test memory persistence`() {
        val inMemoryLocationPersistenceSource = InMemoryLocationPersistenceSource()
        assertTrue("at start location is empty", inMemoryLocationPersistenceSource.getPersistedLocations().isEmpty())

        inMemoryLocationPersistenceSource.saveNewLocation(Location(95.0, 23.2, Date()))
        assertTrue("adding, there are some locations", inMemoryLocationPersistenceSource.getPersistedLocations().isNotEmpty())
    }
}
