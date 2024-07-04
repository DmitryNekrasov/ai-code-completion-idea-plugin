package com.aicc.aicodecompletionideaplugin

import com.intellij.codeInsight.inline.completion.*
import com.intellij.codeInsight.inline.completion.elements.InlineCompletionElement
import com.intellij.codeInsight.inline.completion.elements.InlineCompletionGrayTextElement
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch

class AICCInlineCompletionProvider : InlineCompletionProvider {
    override val id = InlineCompletionProviderID("AICCInlineCompletionProvider")

    override suspend fun getSuggestion(request: InlineCompletionRequest): InlineCompletionSuggestion {
        val startTime = System.nanoTime()
        return InlineCompletionSuggestion.Default(
            channelFlow {
                val (prefix, suffix) = request.document.text.splitUsingOffset(request.startOffset)
                val suggestion = OllamaLLM.call(prefix, suffix) ?: ""
                launch {
                    try {
                        trySend(InlineCompletionGrayTextElement(suggestion))
                    } catch (e: Exception) {
                        println("Inline completion suggestion dispatch failed")
                    }
                }
            }.onCompletion {
                val endTime = System.nanoTime()
                val duration = (endTime - startTime) / 1_000_000  // Convert to milliseconds
                AICCStatistic.onCompletion(duration)
            }
        )
    }

    override val insertHandler: InlineCompletionInsertHandler
        get() = object : InlineCompletionInsertHandler {
            override fun afterInsertion(
                environment: InlineCompletionInsertEnvironment,
                elements: List<InlineCompletionElement>
            ) {
                AICCStatistic.onSuccess()
            }
        }

    override fun isEnabled(event: InlineCompletionEvent): Boolean {
        return event is InlineCompletionEvent.DocumentChange || event is InlineCompletionEvent.DirectCall
    }

    private fun String.splitUsingOffset(offset: Int): Pair<String, String> {
        return substring(0, offset + 1) to substring(offset + 1)
    }
}