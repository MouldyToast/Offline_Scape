package org.jesse.game.content.araxxor.items.venom

import org.jesse.game.item.Item
import org.jesse.game.item.ids.*
import org.jesse.game.model.item.ItemOnNPCAction
import org.jesse.game.world.entity.npc.NPC
import org.jesse.game.npc.ids.*
import org.jesse.game.npc.ids.NID
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.container.RequestResult
import org.jesse.game.world.entity.player.dialogue.dialogue

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-11-17
 */
class CoagulatedVenom: ItemOnNPCAction {
    override fun handleItemOnNPCAction(player: Player?, item: Item?, slot: Int, npc: NPC?) {
        // null checks
        player ?: return; item ?: return; npc ?: return

        if (player.inventory.deleteItem(item).result == RequestResult.SUCCESS) {
            player.attributes["nid_rax_metamorph"] = true
            npc.setTransformation(NID_13683)
            player.dialogue {
                item(COAGULATED_VENOM,
                    "Congratulations! You've unlocked a new metamorphosis option for your pet.")
            }
        }
    }

    override fun getItems(): Array<Any> = arrayOf(COAGULATED_VENOM)
    // We wanna hit both, for anyone with the pet, prior to this update
    override fun getObjects(): Array<Any> = arrayOf(NID_13683, NID)
}