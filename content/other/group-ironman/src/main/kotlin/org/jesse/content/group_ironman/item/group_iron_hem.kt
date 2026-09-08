package org.jesse.content.group_ironman.item

import org.jesse.content.group_ironman.player.groupIronHelmTeleportLastTime
import org.jesse.game.content.skills.magic.spells.teleports.Teleport
import org.jesse.game.content.skills.magic.spells.teleports.TeleportType
import org.jesse.game.item.Item
import org.jesse.game.world.entity.Location
import java.util.concurrent.TimeUnit
import org.jesse.scripts.item.actions.ItemActionScript
import org.jesse.game.item.ids.*
import org.jesse.game.model.item.*

class GroupIronHemItemaction : ItemActionScript() {

    val cooldownSeconds = 30

    init {
        items(GROUP_IRON_HELM, HARDCORE_GROUP_IRON_HELM)


        "teleport" {
            val now = System.currentTimeMillis()
            val previous = player.groupIronHelmTeleportLastTime
            val timeSince = TimeUnit.MILLISECONDS.toSeconds(now - previous)
            if (timeSince >= cooldownSeconds) {
                player.groupIronHelmTeleportLastTime = System.currentTimeMillis()
                val teleport: Teleport = object : Teleport {
                    override fun getType(): TeleportType  = TeleportType.NEAR_REALITY_PORTAL_TELEPORT
                    override fun destination(): Location = Location(3104, 3029, 0)
                    override fun getLevel(): Int  = 0
                    override fun getExperience(): Double = 0.0
                    override fun getRandomizationDistance(): Int = 0
                    override fun getRunes(): Array<Item> = emptyArray()
                    override fun getWildernessLevel(): Int = 0
                    override fun isCombatRestricted(): Boolean = false
                }
                teleport.teleport(player)
            } else {
                val waitSeconds = cooldownSeconds - timeSince
                // TODO: find correct dialogue
                player.sendMessage("You must wait $waitSeconds more seconds.")
            }
        }
    }
}
