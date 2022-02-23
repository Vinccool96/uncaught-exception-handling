package io.github.vinccool96.uncaught

import kotlin.test.Test
import kotlin.test.assertNotSame
import kotlin.test.assertSame

class Tests {

    @Test
    fun testBaseHandler() {
        try {
            throw RuntimeException()
        } catch (e: Throwable) {
            UncaughtExceptionHandler.threadProperties.handler.uncaughtException(e)
        }
    }

    @Test
    fun testSame() {
        val p1 = UncaughtExceptionHandler.threadProperties
        val p2 = UncaughtExceptionHandler.threadProperties
        val h1 = UncaughtExceptionHandler.threadProperties.handler
        val h2 = UncaughtExceptionHandler.threadProperties.handler

        assertSame(p1, p2)
        assertSame(h1, h2)
    }

    @Test
    fun testChange() {
        val h1 = Handler { }
        assertNotSame(h1, UncaughtExceptionHandler.threadProperties.handler)
        UncaughtExceptionHandler.threadProperties.handler = h1
        assertSame(h1, UncaughtExceptionHandler.threadProperties.handler)
    }

}