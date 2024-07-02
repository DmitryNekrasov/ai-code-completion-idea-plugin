package com.aicc.aicodecompletionideaplugin

import com.intellij.codeInsight.inline.completion.*
import com.intellij.codeInsight.inline.completion.elements.InlineCompletionGrayTextElement

class AICCInlineCompletionProvider : InlineCompletionProvider {
    override val id = InlineCompletionProviderID("AICCInlineCompletionProvider")

    override suspend fun getSuggestion(request: InlineCompletionRequest): InlineCompletionSuggestion {
        val offset = request.startOffset
        val code = request.document.text
        val prefix = code.substring(0, offset + 1)
        val suffix = code.substring(offset + 1)

        var suggestion = ""
        OllamaLLM.call(prefix, suffix)?.also { response ->
            suggestion = response
        }

        return InlineCompletionSuggestion.withFlow {
            emit(InlineCompletionGrayTextElement(suggestion))
        }
    }

    override fun isEnabled(event: InlineCompletionEvent): Boolean {
        return event is InlineCompletionEvent.DocumentChange || event is InlineCompletionEvent.DirectCall
    }
}