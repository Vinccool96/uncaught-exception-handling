package io.github.vinccool96.uncaught

import kotlin.test.Test
import kotlin.test.assertNotSame
import kotlin.test.assertSame
import kotlin.test.assertTrue

class JVMTests {

    private val res = BooleanArray(5)

    @Test
    fun testMultipleThreadsDifferentResult() {
        val threads = Array(5) { i: Int ->
            Thread {
                UncaughtExceptionHandler.threadProperties.handler = Handler { e ->
                    res[i] = true
                    e.printStackTrace()
                }
                try {
                    throw RuntimeException()
                } catch (e: Throwable) {
                    UncaughtExceptionHandler.threadProperties.handler.uncaughtException(e)
                }
            }
        }
        for (thread in threads) {
            thread.start()
        }
        for (thread in threads) {
            thread.join()
        }
        for (b in res) {
            assertTrue(b)
        }
    }

    @Test
    fun testMultipleThreadsBase() {
        val p0 = ThreadProperties { }
        val properties = Array(5) { p0 }

        for (i in 0 until properties.size - 1) {
            for (j in i + 1 until properties.size) {
                assertSame(properties[i], properties[j])
            }
        }

        val threads = Array(5) { i: Int ->
            Thread {
                properties[i] = UncaughtExceptionHandler.threadProperties
            }
        }
        for (thread in threads) {
            thread.start()
        }
        for (thread in threads) {
            thread.join()
        }

        for (i in 0 until properties.size - 1) {
            for (j in i + 1 until properties.size) {
                assertNotSame(properties[i], properties[j])
            }
        }
    }

}