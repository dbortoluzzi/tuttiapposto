package it.dbortoluzzi.tuttiapposto.framework

import android.util.Log
import it.dbortoluzzi.domain.util.ServiceResult
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit


interface Cache<T : Any> {
    fun get(key: String): T?
    fun put(key: String, value: T)
}

class TimedCache<T : Any>() : Cache<T> {
    private var cacheTimeValidityInMillis: Long = 0
    private val hashMap = ConcurrentHashMap<String, TimedEntry<T>>()

    companion object {
        val TAG = "TimedCache"
        fun <T : Any> expiringEvery(duration: Long, timeUnit: TimeUnit) =
                TimedCache<T>().apply {
                    cacheTimeValidityInMillis = TimeUnit.MILLISECONDS.convert(duration, timeUnit)
                }
    }

    override fun get(key: String): T? {
        val timedEntry = hashMap[key]
        if (timedEntry == null || timedEntry.isExpired()) {
            Log.d(TAG, "cache miss for key $key")
            return null
        }

        return timedEntry.value
    }

    override fun put(key: String, value: T) {
        Log.d(TAG, "caching $key with value $value")
        hashMap[key] = TimedEntry(value, cacheTimeValidityInMillis)
    }

    data class TimedEntry<T : Any>(val value: T, val maxDurationInMillis: Long) {
        private val creationTime: Long = now()
        fun isExpired() = (now() - creationTime) > maxDurationInMillis

        private fun now() = System.currentTimeMillis()
    }
}

suspend fun <T : Any>TimedCache<T>.getOrElse(key: String, f: suspend () -> ServiceResult<T>): ServiceResult<T> {
    val resultCached: T? = this.get(key)
    if (resultCached == null) {
        val result = f()
        if (result is ServiceResult.Success) {
            put(key, result.data)
            return ServiceResult.Success(result.data)
        } else {
            return result
        }
    } else {
        return ServiceResult.Success(resultCached)
    }
}