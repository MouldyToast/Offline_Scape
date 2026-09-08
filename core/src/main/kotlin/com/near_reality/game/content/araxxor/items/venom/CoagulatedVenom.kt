package com.near_reality.game.content.araxxor.items.venom

import com.zenyte.game.item.Item
import com.zenyte.game.item.ids.*
import com.zenyte.game.model.item.ItemOnNPCAction
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.container.RequestResult
import com.zenyte.game.world.entity.player.dialogue.dialogue

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
            npc.setTransformation(NpcId.NID_13683)
            player.dialogue {
                item(COAGULATED_VENOM,
                    "Congratulations! You've unlocked a new metamorphosis option for your pet.")
            }
        }
    }

    override fun getItems(): Array<Any> = arrayOf(COAGULATED_VENOM)
    // We wanna hit both, for anyone with the pet, prior to this update
    override fun getObjects(): Array<Any> = arrayOf(NpcId.NID_13683, NpcId.NID)
}