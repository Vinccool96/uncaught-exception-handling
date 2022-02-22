package io.github.vinccool96.uncaught

fun interface Handler {

    fun uncaughtException(e: Throwable)

}