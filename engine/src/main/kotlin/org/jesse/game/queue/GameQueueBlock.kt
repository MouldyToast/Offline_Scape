package org.jesse.game.queue

@JvmInline
value class GameQueueBlock(val block: suspend () -> Unit)