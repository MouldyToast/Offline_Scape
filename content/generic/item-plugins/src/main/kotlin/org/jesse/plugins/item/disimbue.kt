package org.jesse.plugins.item

import org.jesse.game.content.imbue.DisimbueItemHandler
import org.jesse.game.model.item.enums.ImbueableItem
import org.jesse.scripts.item.actions.ItemActionScript
import org.jesse.game.item.ids.*
import org.jesse.game.model.item.*

class DisimbueItemaction : ItemActionScript() {

    init {
        items(ImbueableItem.IMBUEABLES
            .filterNot {
                it.value.name.contains("SLAYER_HELM") ||
                        it.value.name.contains("BLACK_MASK") ||
                        it.value.name.startsWith("CRYSTAL_") ||
                        it.value == ImbueableItem.RING_OF_SUFFERING ||
                        it.value == ImbueableItem.RING_OF_SUFFERING_RI
            }
            .keys)

        "Uncharge" {
            DisimbueItemHandler.disimbueItem(player, item)
        }
    }
}
