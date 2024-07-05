package com.aicc.aicodecompletionideaplugin

import java.util.concurrent.atomic.AtomicInteger

/**
 * Manages cache statistics for the AI Code Completion plugin, specifically tracking cache hits and misses.
 * A cache hit occurs when a code completion suggestion is found in the cache, while a cache miss occurs when it is not.
 * This object is responsible for incrementing the count of cache hits and misses and updating the AICC status bar widget
 * with the latest statistics to provide real-time feedback to the user.
 */
object AICCCacheStatistic {
    /**
     * Increments the count of cache misses and updates the AICC status bar widget.
     * This method should be called whenever a code completion suggestion request results in a cache miss.
     */
    fun onCacheMiss() {
        cacheMissNumber.incrementAndGet()
        AICCStatusBarWidgetManager.updateCacheMissNumber(cacheMissNumber.get())
    }

    /**
     * Increments the count of cache hits and updates the AICC status bar widget.
     * This method should be called whenever a code completion suggestion request is successfully served from the cache.
     */
    fun onCacheHit() {
        cacheHitNumber.incrementAndGet()
        AICCStatusBarWidgetManager.updateCacheHitNumber(cacheHitNumber.get())
    }

    private val cacheMissNumber = AtomicInteger(0)
    private val cacheHitNumber = AtomicInteger(0)
}