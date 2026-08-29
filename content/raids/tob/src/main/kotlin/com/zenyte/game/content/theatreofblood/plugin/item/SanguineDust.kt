package com.zenyte.game.content.theatreofblood.plugin.item

import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId.SANGUINE_DUST
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

    override fun getObjects(): Array<Any> = arrayOf(NpcId.LIL_ZIK_8337)
}