package com.aicc.aicodecompletionideaplugin

fun String.shouldBeSkippedOnPosition(offset: Int) = checkElementUnderCaret(this, offset) {
    afterSemicolon().also { if (it) println("afterSemicolon") }
            || afterLBrace().also { if (it) println("afterLbrace") }
            || afterRBrace().also { if (it) println("afterRbrace") }
            || beforeLParenthesis().also { if (it) println("beforeLParenthesis") }
            || afterRParenthesis().also { if (it) println("afterRParenthesis") }
            || insideIdentifier().also { if (it) println("insideIdentifier") }

}

private fun Pair<Char, Char>.afterSemicolon(): Boolean {
    return first == ';'
}

private fun Pair<Char, Char>.afterLBrace(): Boolean {
    return first == '{'
}

private fun Pair<Char, Char>.afterRBrace(): Boolean {
    return first == '}'
}

private fun Pair<Char, Char>.beforeLParenthesis(): Boolean {
    return second == '('
}

private fun Pair<Char, Char>.afterRParenthesis(): Boolean {
    return first == ')'
}

private fun Pair<Char, Char>.insideIdentifier(): Boolean {
    return first.isLetterOrDigit() && second.isLetterOrDigit()
}

private fun checkElementUnderCaret(text: String, offset: Int, satisfySkippedCriteria: Pair<Char, Char>.() -> Boolean): Boolean {
    val beforeCaretChar = if (offset in 0..text.lastIndex) text[offset] else '\n'
    val afterCaretChar = if (offset + 1 in 0..text.lastIndex) text[offset + 1] else '\n'
    return (beforeCaretChar to afterCaretChar).satisfySkippedCriteria()
}