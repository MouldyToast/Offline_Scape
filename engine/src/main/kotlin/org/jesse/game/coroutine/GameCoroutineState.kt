package org.jesse.game.coroutine

interface GameCoroutineState<T> {

    fun resume(): Boolean

    fun get(): T

}