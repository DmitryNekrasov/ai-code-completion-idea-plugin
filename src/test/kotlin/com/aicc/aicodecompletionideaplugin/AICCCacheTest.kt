package com.aicc.aicodecompletionideaplugin

import kotlin.test.assertEquals
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AICCCacheTest {
    @Test
    fun `test cache contains key for existing key`() {
        val key = "existingKey"
        val value = "value"
        AICCCache[key] = value

        assertTrue(key in AICCCache, "Cache should contain the key that was inserted.")
    }

    @Test
    fun `test cache does not contain key for missing key`() {
        val missingKey = "nonExistingKey"

        assertFalse(missingKey in AICCCache, "Cache should not contain a key that was not inserted.")
    }

    @Test
    fun `test cache insertion and retrieval`() {
        val key = "testKey"
        val value = "testValue"
        AICCCache[key] = value

        assertEquals(value, AICCCache[key], "Cache should return the value that was inserted.")
    }

    @Test
    fun `test cache miss returns EMPTY`() {
        val missingKey = "missingKey"

        assertEquals("EMPTY", AICCCache[missingKey], "Cache should return EMPTY for missing keys.")
    }

    @Test
    fun `test key transformation logic`() {
        val originalKey = "Key \nto\n    be   \n   converted\n   "
        val value = "testValue"
        AICCCache[originalKey] = value

        val expectedKey = "Key to be converted" // Expected result after applying toKey() transformations
        assertEquals(value, AICCCache[expectedKey], "Cache should return the value that was inserted.")
    }
}