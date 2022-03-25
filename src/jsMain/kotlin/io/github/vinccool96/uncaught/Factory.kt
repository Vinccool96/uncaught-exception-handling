package io.github.vinccool96.uncaught

internal actual object Factory {

    private val properties: ThreadProperties by lazy {
        ThreadProperties { e: Throwable ->
            console.warn(e.stackTraceToString())
        }
    }

    actual fun getThreadProperties(): ThreadProperties {
        return properties
    }

}