package com.aicc.aicodecompletionideaplugin

import kotlin.test.*

class AICCStatisticAnalyzerTest {
    @BeforeTest
    fun setup() {
        AICCStatistic.reset()
    }

    @Test
    fun `makeSingleLineIfNeeded converts multi-line suggestion to single line when thresholds are met`() {
        repeat(100) {
            AICCStatistic.onCompletion(1000)
            if (it % 4 == 0) {
                AICCStatistic.onSuccess()
            }
        }

        val multiLineSuggestion = "first line;\nsecond line"
        val expected = "first line;"
        val result = AICCStatisticAnalyzer.makeSingleLineIfNeeded(multiLineSuggestion)
        assertEquals(expected, result)
    }

    @Test
    fun `makeSingleLineIfNeeded keeps multi-line suggestion when thresholds are not met`() {
        repeat(15) {
            AICCStatistic.onCompletion(1000)
            AICCStatistic.onSuccess()
        }

        val multiLineSuggestion = "first line;\nsecond line"
        val result = AICCStatisticAnalyzer.makeSingleLineIfNeeded(multiLineSuggestion)
        assertEquals(multiLineSuggestion, result)
    }

    @Test
    fun `makeSingleLineIfNeeded keeps single-line suggestion unchanged`() {
        repeat(100) {
            AICCStatistic.onCompletion(1000)
            if (it % 4 == 0) {
                AICCStatistic.onSuccess()
            }
        }

        val singleLineSuggestion = "single line;"
        val result = AICCStatisticAnalyzer.makeSingleLineIfNeeded(singleLineSuggestion)
        assertEquals(singleLineSuggestion, result)
    }
}