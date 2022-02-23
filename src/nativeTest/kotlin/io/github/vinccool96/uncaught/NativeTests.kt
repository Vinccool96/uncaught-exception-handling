package io.github.vinccool96.uncaught

import kotlin.native.concurrent.TransferMode
import kotlin.native.concurrent.Worker
import kotlin.native.concurrent.freeze
import kotlin.test.Test
import kotlin.test.assertNotSame
import kotlin.test.assertSame
import kotlin.test.assertTrue

class NativeTests {

    @Test
    fun testMultipleThreadsDifferentResult() {
        val res = Array(5) { Container() }
        val workers = Array(5) { Worker.start() }
        val threads = Array(5) { i: Int ->
            workers[i].execute(TransferMode.SAFE, {}, {
                var r = false
                UncaughtExceptionHandler.threadProperties.handler = Handler { e: Throwable ->
                    r = true
                    e.printStackTrace()
                }
                try {
                    throw RuntimeException()
                } catch (e: Throwable) {
                    UncaughtExceptionHandler.threadProperties.handler.uncaughtException(e)
                }
                r
            })
        }
        for (threadIdx in threads.withIndex()) {
            threadIdx.value.consume { b -> res[threadIdx.index].b = b }
        }
        for (b in res) {
            assertTrue(b.b)
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

        val workers = Array(5) { Worker.start() }
        val threads = Array(5) { i: Int ->
            workers[i].execute(TransferMode.SAFE, {}, {
                UncaughtExceptionHandler.threadProperties.freeze()
                UncaughtExceptionHandler.threadProperties
            })
        }
        for (threadIdx in threads.withIndex()) {
            threadIdx.value.consume { p -> properties[threadIdx.index] = p }
        }

        for (i in 0 until properties.size - 1) {
            for (j in i + 1 until properties.size) {
                assertNotSame(properties[i], properties[j])
            }
        }
    }

    private class Container(var b: Boolean = false)

}