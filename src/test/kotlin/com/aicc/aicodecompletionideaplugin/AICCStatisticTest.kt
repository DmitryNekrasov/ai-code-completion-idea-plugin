package com.aicc.aicodecompletionideaplugin

import kotlin.test.BeforeTest
import kotlin.test.assertEquals
import kotlin.test.Test

class AICCStatisticTest {
    @BeforeTest
    fun reset() {
        AICCStatistic.reset()
    }

    @Test
    fun testReset() {
        assertEquals(0, AICCStatistic.totalCompletionsNumber)
        assertEquals(0, AICCStatistic.successfulCompletionsNumber)
        assertEquals(0, AICCStatistic.userEngagement)
        assertEquals(0.0, AICCStatistic.averageCompletionTime)
    }

    @Test
    fun `test on completion`() {
        val n = 10
        for (i in 1..n) {
            AICCStatistic.onCompletion(1000)
        }
        assertEquals(n, AICCStatistic.totalCompletionsNumber)
    }

    @Test
    fun `test on success`() {
        val n = 10
        for (i in 1..n) {
            AICCStatistic.onCompletion(1000)
            if (i % 2 == 0) {
                AICCStatistic.onSuccess()
            }
        }
        assertEquals(n, AICCStatistic.totalCompletionsNumber)
        assertEquals(n / 2, AICCStatistic.successfulCompletionsNumber)
    }

    @Test
    fun `test user engagement`() {
        val n = 10
        for (i in 1..n) {
            AICCStatistic.onCompletion(1000)
            if (i % 2 == 0) {
                AICCStatistic.onSuccess()
            }
        }
        assertEquals(n, AICCStatistic.totalCompletionsNumber)
        assertEquals(50, AICCStatistic.userEngagement)
    }

    @Test
    fun `test average completion time`() {
        val n = 10
        for (i in 1..n) {
            AICCStatistic.onCompletion(i * 1000L)
        }
        assertEquals(n, AICCStatistic.totalCompletionsNumber)
        assertEquals((n + 1.0) / 2, AICCStatistic.averageCompletionTime)
    }
}