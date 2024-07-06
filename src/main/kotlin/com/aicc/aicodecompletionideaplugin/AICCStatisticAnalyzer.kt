package com.aicc.aicodecompletionideaplugin

/**
 * Analyzes statistics related to AI code completion to provide insights and potentially adjust completion strategies.
 * This includes making suggestions single-line under certain conditions to improve user engagement.
 */
object AICCStatisticAnalyzer {
    /**
     * Converts a multi-line suggestion to a single line if certain thresholds are met.
     * This is based on the total number of completions and user engagement percentage.
     * The aim is to enhance readability and focus on the most relevant part of the suggestion.
     *
     * @param suggestion The original suggestion string, which may be multi-line.
     * @return A single-line suggestion if thresholds are met, otherwise returns the original suggestion.
     */
    fun makeSingleLineIfNeeded(suggestion: String): String {
        if (AICCStatistic.totalCompletionsNumber > TOTAL_COMPLETIONS_NUMBER_THRESHOLD &&
            AICCStatistic.userEngagement < USER_ENGAGEMENT_THRESHOLD &&
            '\n' in suggestion
        ) {
            val line = suggestion.substringBefore('\n')
            if (line.endsWith(';')) {
                return line
            }
        }
        return suggestion
    }

    private const val TOTAL_COMPLETIONS_NUMBER_THRESHOLD = 20
    private const val USER_ENGAGEMENT_THRESHOLD = 30
}