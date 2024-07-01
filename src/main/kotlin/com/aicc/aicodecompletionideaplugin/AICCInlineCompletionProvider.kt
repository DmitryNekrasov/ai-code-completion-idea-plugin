package com.aicc.aicodecompletionideaplugin

import com.intellij.codeInsight.inline.completion.*
import com.intellij.codeInsight.inline.completion.elements.InlineCompletionGrayTextElement

class AICCInlineCompletionProvider : InlineCompletionProvider {
    override val id = InlineCompletionProviderID("AICCInlineCompletionProvider")

    override suspend fun getSuggestion(request: InlineCompletionRequest): InlineCompletionSuggestion {
        val offset = request.startOffset
        val text = request.document.text.substring(0..offset)
        val lastLine = text.lines().last()

        var suggestion = ""
        for (i in 0..EXAMPLE_STRING.lastIndex) {
            val prefix = EXAMPLE_STRING.substring(0..i)
            if (lastLine.endsWith(prefix)) {
                val suffix = EXAMPLE_STRING.substring(i + 1)
                println("prefix = $prefix")
                println("suffix = $suffix")
                suggestion = suffix
                break
            }
        }

        return InlineCompletionSuggestion.withFlow {
            emit(InlineCompletionGrayTextElement(suggestion))
        }
    }

    override fun isEnabled(event: InlineCompletionEvent): Boolean {
        return event is InlineCompletionEvent.DocumentChange || event is InlineCompletionEvent.DirectCall
    }

    companion object {
        const val EXAMPLE_STRING = "System.out.println(sum);"
    }
}