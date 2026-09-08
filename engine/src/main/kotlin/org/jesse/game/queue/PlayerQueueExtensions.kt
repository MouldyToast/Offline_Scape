package org.jesse.game.queue

import org.jesse.game.world.entity.player.Player

fun Player.weakQueue(block: suspend () -> Unit) = queueStack.queue(QueueType.Weak, block)

fun Player.normalQueue(block: suspend () -> Unit) = queueStack.queue(QueueType.Normal, block)

fun Player.strongQueue(block: suspend () -> Unit) = queueStack.queue(QueueType.Strong, block)