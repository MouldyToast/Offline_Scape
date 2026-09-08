package org.jesse.game.content.theatreofblood.plugin.item

import org.jesse.game.item.Item
import org.jesse.game.item.ids.*
import org.jesse.game.model.item.ItemOnNPCAction
import org.jesse.game.world.entity.npc.NPC
import org.jesse.game.npc.ids.*
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.container.RequestResult
import org.jesse.game.world.entity.player.dialogue.dialogue

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-12-10
 */
class SanguineDust : ItemOnNPCAction {
    override fun handleItemOnNPCAction(player: Player?, item: Item?, slot: Int, npc: NPC?) {
        // null checks
        player ?: return; item ?: return; npc ?: return

        if (player.inventory.deleteItem(item).result == RequestResult.SUCCESS) {
            player.attributes["tob_pet_metamorph"] = true
            player.dialogue {
                item(
                    SANGUINE_DUST,
                    "Congratulations! You've unlocked a new metamorphosis option for your pet.")
            }
        }
    }

    override fun getItems(): Array<Any> = arrayOf(SANGUINE_DUST)

    override fun getObjects(): Array<Any> = arrayOf(LIL_ZIK_8337)
}