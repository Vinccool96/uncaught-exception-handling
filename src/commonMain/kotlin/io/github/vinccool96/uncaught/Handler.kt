package io.github.vinccool96.uncaught

/**
 * The functional interface that handles the exceptions
 */
fun interface Handler {

    /**
     * It specifies how the exception [e] should be handled
     *
     * @param e the exception to handle
     */
    fun uncaughtException(e: Throwable)

}