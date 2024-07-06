/**
 * Provides functionality to analyze the caret position within the code editor and determine if the current position
 * should be skipped for code completion suggestions. This determination is based on the syntactical context of the
 * caret position.
 *
 * It covers the next cases:
 *
 * 1. If caret is inside literal:
 *      int x = 12|3;
 *      String str = "Hell|o, world!";
 *
 * 2. If caret is inside identifier or keyword:
 *      int myAm|azingVariable = 123;
 *      System.out.prin|tln();
 *      bool|ean f = true;
 *
 * 3. If caret is after semicolon:
 *      int x = 123;|
 *
 * 4. If caret is after left brace:
 *      if (condition) {|
 *
 * 5. If caret is after right brace:
 *      if (condition) {}|
 *
 * 6. If caret is before left parenthesis:
 *      System.out.println|();
 *
 * 7. If caret is after right parenthesis:
 *     static void myMethod()|
 */
package com.aicc.aicodecompletionideaplugin

fun String.shouldBeSkippedOnPosition(offset: Int) = checkElementUnderCaret(this, offset) {
    afterSemicolon()
            || afterLBrace()
            || afterRBrace()
            || beforeLParenthesis()
            || afterRParenthesis()
            || insideIdentifier()

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

private fun checkElementUnderCaret(
    text: String,
    offset: Int,
    satisfySkippedCriteria: Pair<Char, Char>.() -> Boolean
): Boolean {
    val beforeCaretChar = if (offset in 0..text.lastIndex) text[offset] else '\n'
    val afterCaretChar = if (offset + 1 in 0..text.lastIndex) text[offset + 1] else '\n'
    return (beforeCaretChar to afterCaretChar).satisfySkippedCriteria()
}