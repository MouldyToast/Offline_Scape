package com.near_reality.game.content.chaoskey

import com.zenyte.game.item.ids.*
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.variables.TickVariable
import com.zenyte.game.world.flooritem.FloorItem
import com.zenyte.plugins.flooritem.FloorItemPlugin


class ChaosKeyFloorItem : FloorItemPlugin {

    override fun handle(player: Player, item: FloorItem?, optionId: Int, option: String) {
        if (option.equals("take", ignoreCase = true)) {
            if (player.getSkills().getCombatLevel() < 126) {
                player.sendMessage("You need to have a combat level of 126 to pick this key up.")
                return
            }
            player.variables.setSkull(true)
            if (player.variables.getTime(TickVariable.TELEBLOCK) <= 0 && player.variables.getTime(TickVariable.TELEBLOCK_IMMUNITY) <= 0) {
                player.variables.schedule(500, TickVariable.TELEBLOCK)
                player.variables.schedule(600, TickVariable.TELEBLOCK_IMMUNITY)
                player.sendMessage("<col=4f006f>You have been tele-blocked for looting the Chaos Key. It will expire in 5 minutes</col>.")
            }
            World.takeFloorItem(player, item)
        }
    }

    override fun getItems(): IntArray {
        return intArrayOf(CHAOS_KEY_ACTIVE)
    }

    override fun overrideTake(): Boolean {
        return true
    }

    override fun canTelegrab(player: Player, item: FloorItem): Boolean {
        return false
    }

    override fun telegrab(player: Player, item: FloorItem) {
    }
}