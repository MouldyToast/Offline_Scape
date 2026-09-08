package org.jesse.game.content.boss.nex.item

import org.jesse.game.content.skills.smithing.Smelting
import org.jesse.game.item.Item
import org.jesse.game.item.ids.*
import org.jesse.game.model.item.ItemOnObjectAction
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.container.RequestResult
import org.jesse.game.world.entity.player.dialogue.dialogue
import org.jesse.game.world.entity.player.dialogue.options
import org.jesse.game.obj.ids.*
import org.jesse.game.world.`object`.WorldObject

/**
 * Represents an [ItemOnObjectAction] that handles using bandos on the ancient forge.
 */
class BandosOnAncientForge : ItemOnObjectAction {

    override fun handleItemOnObjectAction(player: Player, item: Item, slot: Int, `object`: WorldObject) {

        val componentsAmount = componentsCount(item.id)
        val inventory = player.inventory
        if (inventory.freeSlots < componentsAmount){
            player.dialogue { plain("Not enough space in your inventory to melt down the armour.") }
            return
        }
        val componentsString = componentsString(componentsAmount)

        player.options("Melt down your ${item.name} into $componentsString?") {
            "Yes." {
                if (inventory.deleteItem(item).result == RequestResult.SUCCESS) {
                    val components = Item(BANDOSIAN_COMPONENTS, componentsAmount)
                    inventory.addItem(components)
                    player.animation = Smelting.ANIMATION
                    player.dialogue {
                        item(components, "You use the forge to melt the armour down into $componentsString.")
                    }
                }
            }
            "No." {}
        }
    }

    override fun getItems() = arrayOf(
        BANDOS_CHESTPLATE,
        BANDOS_TASSETS,
    )

    override fun getObjects() = arrayOf(ANCIENT_FORGE_42966)

    private companion object {
        private fun componentsCount(unNotedItem: Int): Int = when (unNotedItem) {
            BANDOS_CHESTPLATE -> 3
            BANDOS_TASSETS -> 2
            else -> error("Unknown component")
        }
        private fun componentsString(amount: Int) = "$amount Bandosian component${if(amount > 1) "s" else ""}"
    }
}
