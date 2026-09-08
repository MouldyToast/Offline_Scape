package org.jesse.game.queue

sealed class QueueType {

    object Weak : QueueType()

    object Normal : QueueType()

    object Strong : QueueType()

}