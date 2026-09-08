package org.jesse.api.service.store

import org.jesse.game.task.WorldTasksManager
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.dialogue.dialogue

fun Player.notify(message: String, schedule: Boolean = false, loading: Boolean = false) {
    fun notifyPlayerHandle() {
        sendMessage(message)
        dialogue {
            if (loading)
                loading(message)
            else
                plain(message)
        }
    }
    if (schedule)
        WorldTasksManager.schedule { notifyPlayerHandle() }
    else
        notifyPlayerHandle()
}
