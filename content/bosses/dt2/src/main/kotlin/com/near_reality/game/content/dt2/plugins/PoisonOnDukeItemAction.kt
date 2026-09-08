package com.near_reality.game.content.dt2.plugins

import com.near_reality.game.content.dt2.npc.DT2BossDifficulty
import com.near_reality.game.content.dt2.npc.theduke.DukeSucellusEntity
import com.zenyte.game.item.ItemId
import com.zenyte.game.item.Item
import com.zenyte.game.item.ItemId.ARDERMUSCA_POISON
import com.zenyte.game.model.item.ItemOnNPCAction
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.container.RequestResult
import com.zenyte.game.world.entity.player.dialogue.dialogue

@Suppress("unused")
class PoisonOnDukeItemAction : ItemOnNPCAction {
    private val orb = Item(ItemId.AWAKENERS_ORB, 1)
    private val poisonNormal = Item(ARDERMUSCA_POISON, 2)
    private val p2wPoisonNormal = Item(ItemId.POTENT_ARDER_MUSCA_POISON, 2)


    override fun handleItemOnNPCAction(player: Player, item: Item, slot: Int, npc: NPC) {
        if (npc is DukeSucellusEntity) {
            if (player.inventory.deleteItems(*npc.difficulty.getRequiredItems()).result == RequestResult.SUCCESS) {
                npc.disturb(player)
                return
            }
            else if (player.inventory.deleteItems(*npc.difficulty.getAlternateItems()).result == RequestResult.SUCCESS) {
                npc.disturb(player)
                return
            }
        }
        else
            player.dialogue { plain("This wouldn't have any effect") }


    }

    override fun getItems(): Array<Any> {
        return arrayOf(ARDERMUSCA_POISON, ItemId.POTENT_ARDER_MUSCA_POISON, ItemId.AWAKENERS_ORB)
    }

    override fun getObjects(): Array<Any> {
        return arrayOf(12166)
    }

    private fun DT2BossDifficulty.getRequiredItems() : Array<Item> {
        return when (this) {
            DT2BossDifficulty.QUEST -> emptyArray<Item>()
            DT2BossDifficulty.NORMAL -> arrayOf(poisonNormal)
            DT2BossDifficulty.AWAKENED -> arrayOf(poisonNormal)
        }
    }

    private fun DT2BossDifficulty.getAlternateItems() : Array<Item> {
        return when (this) {
            DT2BossDifficulty.QUEST -> emptyArray<Item>()
            DT2BossDifficulty.NORMAL -> arrayOf(p2wPoisonNormal)
            DT2BossDifficulty.AWAKENED -> arrayOf(p2wPoisonNormal)
        }
    }
}