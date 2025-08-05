package com.aicc.aicodecompletionideaplugin

import com.intellij.codeInsight.inline.completion.*
import com.intellij.codeInsight.inline.completion.elements.InlineCompletionElement
import com.intellij.codeInsight.inline.completion.elements.InlineCompletionGrayTextElement
import com.intellij.codeInsight.inline.completion.suggestion.InlineCompletionSingleSuggestion
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import org.jetbrains.concurrency.runAsync

/**
 * Provides inline code completion suggestions by analyzing the current context in the editor. It utilizes both a local
 * cache and an external AI model to generate suggestions, aiming to enhance coding efficiency by suggesting relevant
 * code snippets inline with the user's ongoing coding activity.
 *
 * The provider operates by splitting the current document text around the caret position into a prefix and suffix,
 * using these to query the cache or the AI model for suggestions. Suggestions are then processed to determine if they
 * should be presented as single-line or remain as provided. This decision is made based on the analysis performed by
 * the `AICCStatisticAnalyzer`.
 *
 * Inline completion suggestions are provided as gray text directly in the editor, offering a seamless experience that
 * allows developers to accept suggestions with minimal interruption to their workflow.
 */
class AICCInlineCompletionProvider : InlineCompletionProvider {
    /**
     * Unique identifier for this inline completion provider.
     */
    override val id = InlineCompletionProviderID("AICCInlineCompletionProvider")

    /**
     * Generates and returns an inline completion suggestion based on the current request.
     * It processes the document text around the caret position to generate suggestions,
     * which may be adjusted to single-line format based on certain criteria.
     *
     * @param request The current inline completion request.
     * @return An inline completion suggestion.
     */
    override suspend fun getSuggestion(request: InlineCompletionRequest): InlineCompletionSingleSuggestion {
        val startTime = System.nanoTime()
        return InlineCompletionSuggestion.Default(
            channelFlow {
                // TODO use request.file.name for filename
                val (prefix, suffix) = request.document.text.splitUsingOffset(request.startOffset)
                val lastPrefixLine = prefix.lines().last()
                val suggestion = (if (prefix in AICCCache) {
                    AICCCacheStatistic.onCacheHit()
                    AICCCache[prefix]
                } else if (lastPrefixLine in AICCCache) {
                    AICCCacheStatistic.onCacheHit()
                    AICCCache[lastPrefixLine]
                } else {
                    AICCCacheStatistic.onCacheMiss()
                    OllamaLLM.call(prefix, suffix)?.also {
                        addCurrentToCache(prefix, it)
                        addCurrentToCache(lastPrefixLine, it)
                    }
                } ?: "").let { AICCStatisticAnalyzer.makeSingleLineIfNeeded(it) }
                if (suggestion.isNotBlank()) {
                    runAsync {
                        addNextToCache(prefix, suffix, suggestion)
                    }
                }
                launch {
                    try {
                        trySend(InlineCompletionGrayTextElement(suggestion))
                    } catch (e: Exception) {
                        println("Inline completion suggestion dispatch failed")
                    }
                }
            }.onCompletion {
                val endTime = System.nanoTime()
                val duration = (endTime - startTime) / 1_000_000 // Convert to milliseconds
                AICCStatistic.onCompletion(duration)
            }
        )
    }

    /**
     * Defines the behavior after an inline completion suggestion has been inserted into the document.
     */
    override val insertHandler: InlineCompletionInsertHandler
        get() = object : InlineCompletionInsertHandler {
            /**
             * Called after an inline completion suggestion has been inserted.
             *
             * @param environment The environment in which the insertion occurred.
             * @param elements The elements that were inserted.
             */
            override fun afterInsertion(
                environment: InlineCompletionInsertEnvironment,
                elements: List<InlineCompletionElement>
            ) {
                AICCStatistic.onSuccess()
            }
        }

    /**
     * Determines whether inline completion should be enabled based on the current event.
     * Checks if the text at the caret position is not to be skipped.
     *
     * @param event The current inline completion event.
     * @return `true` if inline completion should be enabled, `false` otherwise.
     */
    override fun isEnabled(event: InlineCompletionEvent): Boolean {
        return event is InlineCompletionEvent.DocumentChange && with(event.editor) {
            !document.text.shouldBeSkippedOnPosition(caretModel.offset)
        }
    }

    /**
     * Splits the document text around the given offset into a prefix and suffix.
     *
     * @param offset The offset around which to split the text.
     * @return A pair consisting of the text before (prefix) and after (suffix) the offset.
     */
    private fun String.splitUsingOffset(offset: Int): Pair<String, String> {
        return substring(0, offset + 1) to substring(offset + 1)
    }

    /**
     * Adds the current suggestion to the cache, associated with the given prefix.
     *
     * @param prefix The prefix for which the suggestion was generated.
     * @param currentSuggestion The suggestion to cache.
     */
    private fun addCurrentToCache(prefix: String, currentSuggestion: String) {
        if (prefix.isNotBlank()) {
            AICCCache[prefix] = currentSuggestion
        }
    }

    /**
     * Caches the next potential suggestion based on the current prefix, suffix, and the current suggestion.
     * This method aims to pre-emptively cache suggestions for a smoother user experience by predicting the next likely
     * input from the user. It concatenates the current suggestion to the prefix, forming a new prefix, and queries
     * for a suggestion based on this new prefix and the original suffix. If a non-blank suggestion is returned, it is
     * cached against the new prefix.
     *
     * @param prefix The current prefix derived from the text before the caret position.
     * @param suffix The current suffix derived from the text after the caret position.
     * @param currentSuggestion The current suggestion that has been generated and potentially inserted by the user.
     */
    private fun addNextToCache(prefix: String, suffix: String, currentSuggestion: String) {
        val nextPrefix = prefix + currentSuggestion
        if (nextPrefix !in AICCCache) {
            OllamaLLM.call(nextPrefix, suffix)?.also {
                if (it.isNotBlank()) {
                    AICCCache[nextPrefix] = it
                }
            }
        }
    }
}
