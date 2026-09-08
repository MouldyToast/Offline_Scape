package org.jesse.game.queue

import org.jesse.game.world.entity.npc.NPC

fun NPC.queue(block: suspend () -> Unit) = queueStack.queue(QueueType.Strong, block)