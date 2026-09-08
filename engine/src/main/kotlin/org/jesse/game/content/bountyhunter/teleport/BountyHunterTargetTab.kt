package org.jesse.game.content.bountyhunter.teleport

import org.jesse.game.content.bountyhunter.isBountyPaired
import org.jesse.game.item.Item
import org.jesse.game.item.ids.*
import org.jesse.game.model.item.pluginextensions.ItemPlugin
import org.jesse.game.world.entity.player.container.RequestResult

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-10-14
 */
class BountyHunterTargetTab : ItemPlugin() {
    override fun handle() {
        bind("Break") { player, item, _ ->
            if (player.isBountyPaired()) {
                if (TeleportToTarget.canTeleportToTarget(player)) {
                    if (player.inventory.deleteItem(Item(item.id, 1)).result == RequestResult.SUCCESS)
                        TeleportToTarget.teleportToTarget(player)
                }
                else
                    player.sendMessage("You cannot teleport to your target right now.")
            }
            else
                player.sendMessage("You do not have a target to teleport right now.")
        }
    }

    override fun getItems(): IntArray = intArrayOf(TARGET_TELEPORT)
}