package com.aicc.aicodecompletionideaplugin

import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.google.common.cache.LoadingCache
import java.util.concurrent.TimeUnit

/**
 * A singleton object that represents a cache specifically designed for storing and retrieving code completion suggestions.
 * It utilizes a LoadingCache from Google's Guava library, ensuring efficient access and automatic eviction policies
 * tailored for the caching of code completion suggestions.
 */
object AICCCache {

    /**
     * Clears all entries from the cache.
     */
    fun clear() {
        cache.invalidateAll()
    }

    /**
     * Checks if the cache contains a value for the specified key.
     *
     * @param key The key to check in the cache.
     * @return Boolean indicating whether the key exists in the cache.
     */
    operator fun contains(key: String): Boolean {
        return cache.getIfPresent(key.toKey()) != null
    }

    /**
     * Retrieves the value associated with the specified key from the cache.
     * If the key is not found, it returns a predefined EMPTY string.
     *
     * @param key The key whose associated value is to be returned.
     * @return The value associated with the specified key or EMPTY if the key doesn't exist.
     */
    operator fun get(key: String): String {
        return cache.get(key.toKey())
    }

    /**
     * Inserts or updates the value associated with the specified key in the cache.
     *
     * @param key The key with which the specified value is to be associated.
     * @param value The value to be associated with the specified key.
     */
    operator fun set(key: String, value: String) {
        cache.put(key.toKey(), value)
    }

    /**
     * The underlying LoadingCache from Google's Guava library, configured with maximum size and expiration policy.
     */
    private val cache: LoadingCache<String, String> by lazy {
        CacheBuilder.newBuilder()
            .maximumSize(1000)
            .expireAfterAccess(10, TimeUnit.MINUTES)
            .build(
                object : CacheLoader<String, String>() {
                    override fun load(key: String): String {
                        return EMPTY
                    }
                }
            )
    }

    /**
     * Transforms a string into a suitable cache key by removing newlines, compressing whitespace, and truncating.
     *
     * @return A processed string suitable for use as a cache key.
     */
    private fun String.toKey(): String {
        return replace("\n", "").replace("\\s+".toRegex(), " ").trimEnd().takeLast(200)
    }

    /**
     * A constant representing an empty value in the cache, used as a default for cache misses.
     */
    private const val EMPTY = "EMPTY"
}