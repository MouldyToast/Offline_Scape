package org.jesse.game.content.bountyhunter.teleport

import org.jesse.game.content.bountyhunter.BountyHunter.TELEPORT_TO_TARGET_UNLOCKED_VAR
import org.jesse.game.item.ids.*
import org.jesse.game.model.item.pluginextensions.ItemPlugin
import org.jesse.game.world.entity.player.container.RequestResult
import org.jesse.game.world.entity.player.dialogue.dialogue
import org.jesse.game.world.entity.player.dialogue.options

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-10-14
 */
class BountyHunterTargetScroll : ItemPlugin() {
    override fun handle() {
        bind("Read") { player, item, _ ->
            player.dialogue {
                options {
                    "Read the scroll" {
                        val unlocked = player.varManager.getBitValue(TELEPORT_TO_TARGET_UNLOCKED_VAR)
                        if (unlocked == 0) {
                            if (player.inventory.deleteItem(item).result == RequestResult.SUCCESS) {
                                player.varManager.sendBitInstant(TELEPORT_TO_TARGET_UNLOCKED_VAR, 1)
                                player.sendMessage("You have unlocked the teleport spell.")
                            }
                            else
                                player.sendMessage("There was an error reading the scroll.")
                        }
                        else
                            player.sendMessage("You have already unlocked this spell.")
                    }

                    "Nevermind." {}
                }
            }
        }
    }

    override fun getItems(): IntArray = intArrayOf(BOUNTY_TELEPORT_SCROLL)
}