package com.dosbots.flixme.data.cache

private const val ONE_DAY_IN_MILLIS = 24 * 60 * 60 * 1000L

abstract class CacheValidator {

    open val cachedDataTolerance: Long = ONE_DAY_IN_MILLIS

    suspend fun validateCache(): Boolean {
        val lastSynced = getLastSyncDate() ?: 0L
        val elapsedTime = System.currentTimeMillis() - lastSynced
        if (cachedDataTolerance > elapsedTime) {
            return true
        }
        invalidate()
        return false
    }

    abstract suspend fun getLastSyncDate(): Long?
    abstract suspend fun invalidate()

}