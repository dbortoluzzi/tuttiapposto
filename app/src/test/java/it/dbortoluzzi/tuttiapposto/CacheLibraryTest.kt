package it.dbortoluzzi.tuttiapposto

import it.dbortoluzzi.tuttiapposto.framework.TimedCache
import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.concurrent.TimeUnit

class CacheLibraryTest {

    private val TEST_KEY = "TEST_KEY"

    @Test
    fun `test cache`() {
        val timedCache = TimedCache.expiringEvery<String>(5, TimeUnit.SECONDS)
        timedCache.put(TEST_KEY, "TEST_VALUE")
        assertTrue("retrieving element from cache empty", timedCache.get(TEST_KEY) != null)
        Thread.sleep(6000)
        assertTrue("retrieving element from cache not empty", timedCache.get(TEST_KEY) == null)
    }
}
