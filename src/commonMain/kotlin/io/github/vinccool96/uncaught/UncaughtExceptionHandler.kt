package io.github.vinccool96.uncaught

/**
 * Here is how you get the Handler for the current thread.
 */
object UncaughtExceptionHandler {

    /**
     * This is thread dependant. You will always get the same if you call it in the same thread.
     */
    val threadProperties: ThreadProperties
        get() = Factory.getThreadProperties()

}