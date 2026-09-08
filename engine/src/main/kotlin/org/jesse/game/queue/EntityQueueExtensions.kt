package org.jesse.game.queue

import org.jesse.game.world.entity.Entity

fun Entity.clearQueues() = queueStack.clear()