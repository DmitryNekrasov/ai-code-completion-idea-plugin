package com.aicc.aicodecompletionideaplugin

import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong

/**
 * This object is responsible for tracking statistics related to AI code completions.
 */
class AICCStatistic {
    /**
     * The total number of code completions attempted.
     * This includes both successful and unsuccessful completions.
     */
    val totalCompletionsNumber: Int
        get() = _totalCompletionsNumber.get()

    /**
     * The total number of successful code completions.
     * A completion is considered successful if it was accepted by the user.
     */
    val successfulCompletionsNumber: Int
        get() = _successfulCompletionsNumber.get()

    /**
     * This method is called when a code completion is attempted.
     * It increments the total number of completions and adds the completion time to the total completion time.
     *
     * @param completionTime The time taken for the code completion, in milliseconds.
     */
    fun onCompletion(completionTime: Long) {
        _totalCompletionsNumber.incrementAndGet()
        _totalCompletionTimeInMillis.addAndGet(completionTime)
    }

    /**
     * This method is called when a code completion is successful.
     * It increments the number of successful completions by one.
     */
    fun onSuccess() {
        _successfulCompletionsNumber.incrementAndGet()
    }

    private val _totalCompletionsNumber = AtomicInteger(0)
    private val _successfulCompletionsNumber = AtomicInteger(0)
    private val _totalCompletionTimeInMillis = AtomicLong(0)
}