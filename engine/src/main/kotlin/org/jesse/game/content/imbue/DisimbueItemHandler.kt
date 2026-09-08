package org.jesse.game.content.imbue

import org.jesse.game.item.Item
import org.jesse.game.item.ids.*
import org.jesse.game.model.item.enums.ImbueableItem
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.dialogue.dialogue
import org.jesse.game.world.entity.player.dialogue.options

object DisimbueItemHandler {
    fun disimbueItem(player: Player, item: Item) {
        if(item.id == RING_OF_SUFFERING_RI || item.id == RING_OF_SUFFERING)
            return
        val imbueable = ImbueableItem.get(item.id) ?: return
        player.dialogue {
            options("Remove the Imbued bonus from this item?") {
                dialogueOption("Yes - you will be refunded 1x Scroll of imbuing", noPlayerMessage = true) {
                    val inventory = player.inventory
                    if (inventory.hasSpaceFor(SCROLL_OF_IMBUING)) {
                        if (inventory.containsAnyOf(item.id)) {
                            inventory.deleteItem(item.id, 1)
                            inventory.addOrDrop(imbueable.normal, 1)
                            inventory.addOrDrop(SCROLL_OF_IMBUING)
                        }
                    } else {
                        player.sendMessage("You do not have enough inventory space to receive the Scroll of imbuing.")
                    }
                }
                dialogueOption("No - keep the imbued item.",  noPlayerMessage = true) {

                }
            }
        }
    }
}
