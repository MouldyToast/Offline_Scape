package org.jesse.game.content.dt2.plugins

import org.jesse.game.content.dt2.npc.DT2BossDifficulty
import org.jesse.game.content.dt2.npc.theduke.DukeSucellusEntity
import org.jesse.game.item.ids.*
import org.jesse.game.item.Item
import org.jesse.game.model.item.ItemOnNPCAction
import org.jesse.game.world.entity.npc.NPC
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.container.RequestResult
import org.jesse.game.world.entity.player.dialogue.dialogue

@Suppress("unused")
class PoisonOnDukeItemAction : ItemOnNPCAction {
    private val orb = Item(AWAKENERS_ORB, 1)
    private val poisonNormal = Item(ARDERMUSCA_POISON, 2)


    override fun handleItemOnNPCAction(player: Player, item: Item, slot: Int, npc: NPC) {
        if (npc is DukeSucellusEntity) {
            if (player.inventory.deleteItems(*npc.difficulty.getRequiredItems()).result == RequestResult.SUCCESS) {
                npc.disturb(player)
                return
            }
        }
        else
            player.dialogue { plain("This wouldn't have any effect") }


    }

    override fun getItems(): Array<Any> {
        return arrayOf(ARDERMUSCA_POISON, AWAKENERS_ORB)
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

}