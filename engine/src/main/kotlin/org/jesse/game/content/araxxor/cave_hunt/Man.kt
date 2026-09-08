package org.jesse.game.content.araxxor.cave_hunt

import org.jesse.game.item.Item
import org.jesse.game.item.ids.*
import org.jesse.game.model.item.ItemOnNPCAction
import org.jesse.game.world.entity.ForceTalk
import org.jesse.game.world.entity.masks.Hit
import org.jesse.game.world.entity.masks.HitType
import org.jesse.game.world.entity.npc.NPC
import org.jesse.game.npc.ids.*
import org.jesse.game.world.entity.npc.actions.NPCPlugin
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.container.RequestResult
import org.jesse.game.world.entity.player.dialogue.dialogue

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-11-16
 */
class Man: NPCPlugin(), ItemOnNPCAction {
    override fun handle() {
        bind("Talk-to")  { _, npc -> npc.forceTalk = ForceTalk("I want the adventure to end.") }
    }
    override fun getNPCs(): IntArray = intArrayOf(MAN_13679)

    // Item On Npc
    override fun handleItemOnNPCAction(player: Player?, item: Item?, slot: Int, npc: NPC?) {
        // Null checks
        player ?: return; npc ?: return; item ?: return

        if (item.id == AMULET_OF_RANCOUR) {
            npc.applyHit(Hit(99, HitType.DEFAULT))
            if (player.inventory.deleteItem(AMULET_OF_RANCOUR, 1).result == RequestResult.SUCCESS) {
                player.inventory.addItem(Item(AMULET_OF_RANCOUR_S, 1))
                (player.mapInstance as AraxyteCaveHunt).roomCompleted = true
                npc.forceTalk = ForceTalk("Thank you.")
                player.dialogue { item(AMULET_OF_RANCOUR_S, "You receive a shiny variant of the amulet.") }
            }
        }
    }

    override fun getItems(): Array<Any> = arrayOf(AMULET_OF_RANCOUR)
    override fun getObjects(): Array<Any> = arrayOf(MAN_13679)
}