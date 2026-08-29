package com.near_reality.plugins.item

import com.zenyte.game.item.ItemId
import com.zenyte.game.task.WorldTask
import com.zenyte.game.world.entity.player.Player

class Raids2BypassTask(private val player: Player) : WorldTask {
    override fun run() {
        if (player.temporaryAttributes.getOrDefault("TOB_pending_bypass", false) as Boolean) {
            player.sendMessage("You already have activated an orb for this raid.")
            stop()
        } else {
            if (player.inventory.deleteItem(ItemId.ORB_OF_BLOOD, 1).succeededAmount == 1) {
                player.lock(4)
                player.temporaryAttributes["TOB_bypass_hook"] = true
                player.temporaryAttributes["TOB_pending_bypass"] = true
            }
            stop()
        }
    }
}
