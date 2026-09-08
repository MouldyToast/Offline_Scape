package org.jesse.game.coroutine

import kotlinx.coroutines.CoroutineScope
import kotlin.coroutines.CoroutineContext

class GameCoroutineScope(
    override val coroutineContext: CoroutineContext
) : CoroutineScope by CoroutineScope(coroutineContext)