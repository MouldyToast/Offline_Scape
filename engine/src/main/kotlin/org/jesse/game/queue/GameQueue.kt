package org.jesse.game.queue

import org.jesse.game.coroutine.GameCoroutineTask

@JvmInline
value class GameQueue(val task: GameCoroutineTask)