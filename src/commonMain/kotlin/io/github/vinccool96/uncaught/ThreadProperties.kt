package io.github.vinccool96.uncaught

/**
 * Used to encapsulate the [handler]
 *
 * @property handler how the exceptions will be treated for the current thread
 */
class ThreadProperties internal constructor(var handler: Handler)