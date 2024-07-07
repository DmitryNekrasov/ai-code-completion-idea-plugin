package com.aicc.aicodecompletionideaplugin

import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AICCCaretPositionAnalyzerTest {
    @Test
    fun `test inside literal`() {
        assertTrue("int x = 123;".shouldBeSkippedOnPosition(9))
        assertTrue("String str = \"Hello, world!\";".shouldBeSkippedOnPosition(16))
    }

    @Test
    fun `test inside identifier or keyword`() {
        assertTrue("int myAmazingVariable = 123;".shouldBeSkippedOnPosition(7))
        assertTrue("System.out.println();".shouldBeSkippedOnPosition(15))
        assertTrue("boolean f = true;".shouldBeSkippedOnPosition(4))
    }

    @Test
    fun `test after semicolon`() {
        assertTrue("int x = 123;".shouldBeSkippedOnPosition(11))
    }

    @Test
    fun `test after left brace`() {
        assertTrue("if (condition) {".shouldBeSkippedOnPosition(15))
    }

    @Test
    fun `test after right brace`() {
        assertTrue("if (condition) {}".shouldBeSkippedOnPosition(16))
    }

    @Test
    fun `test before left parenthesis`() {
        assertTrue("System.out.println();".shouldBeSkippedOnPosition(17))
    }

    @Test
    fun `test after right parenthesis`() {
        assertTrue("static void myMethod()".shouldBeSkippedOnPosition(21))
    }

    @Test
    fun `test outside any specific context`() {
        assertFalse("int x = 123;".shouldBeSkippedOnPosition(4))
    }

    @Test
    fun `test at the boundary of string literal`() {
        val str = "String str = \"Hello!\""
        assertTrue(str.shouldBeSkippedOnPosition(14))
        assertTrue(str.shouldBeSkippedOnPosition(20))
    }

    @Test
    fun `test at the boundary of char literal`() {
        val str = "char c = 'a'"
        assertTrue(str.shouldBeSkippedOnPosition(9))
        assertTrue(str.shouldBeSkippedOnPosition(10))
    }
}