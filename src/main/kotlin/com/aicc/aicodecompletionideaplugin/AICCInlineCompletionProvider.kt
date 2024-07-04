package com.aicc.aicodecompletionideaplugin

import com.intellij.codeInsight.inline.completion.*
import com.intellij.codeInsight.inline.completion.elements.InlineCompletionElement
import com.intellij.codeInsight.inline.completion.elements.InlineCompletionGrayTextElement

class AICCInlineCompletionProvider : InlineCompletionProvider {
    override val id = InlineCompletionProviderID("AICCInlineCompletionProvider")

    override suspend fun getSuggestion(request: InlineCompletionRequest): InlineCompletionSuggestion {
        val offset = request.startOffset
        val code = request.document.text
        val prefix = code.substring(0, offset + 1)
        val suffix = code.substring(offset + 1)
        val suggestion = OllamaLLM.call(prefix, suffix) ?: ""
        return InlineCompletionSuggestion.withFlow {
            emit(InlineCompletionGrayTextElement(suggestion))
        }
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
}