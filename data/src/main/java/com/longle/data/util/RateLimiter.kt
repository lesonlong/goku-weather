package com.longle.data.util

import java.util.concurrent.TimeUnit

/**
 * Utility class that decides whether we should fetch some data or not.
 */
class RateLimiter(timeout: Int, timeUnit: TimeUnit) {
    private val timestamps = HashMap<String, Long>()
    private val timeout = timeUnit.toMillis(timeout.toLong())

    @Synchronized
    fun shouldFetch(key: String): Boolean {
        val lastFetched = timestamps[key]
        val now = now()
        if (lastFetched == null) {
            timestamps[key] = now
            return true
        }
        if (now - lastFetched > timeout) {
            timestamps[key] = now
            return true
        }
        return false
    }

    private fun now() = System.currentTimeMillis()

    @Synchronized
    fun reset(key: String) {
        timestamps.remove(key)
    }
}
