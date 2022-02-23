package io.github.vinccool96.uncaught

@ThreadLocal
internal actual object Factory {

    private val properties: ThreadProperties by lazy {
        ThreadProperties { e: Throwable ->
            e.printStackTrace()
        }
    }

    actual fun getThreadProperties(): ThreadProperties {
        return properties
    }

}