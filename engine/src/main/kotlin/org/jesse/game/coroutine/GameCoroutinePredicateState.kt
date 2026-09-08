package org.jesse.game.coroutine

class GameCoroutinePredicateState(
    private val predicate: () -> Boolean
) : GameCoroutineState<Unit> {

    override fun resume() = predicate()

    override fun get() {}

}