package org.jesse.plugins.item.actions.death_items

import org.jesse.game.item.Item
import org.jesse.game.model.item.enums.ImbueableItem
import org.jesse.game.model.item.pluginextensions.ItemDeathStatus
import mgi.types.config.items.ItemDefinitions
import org.jesse.scripts.item.actions.ItemActionScript
import org.jesse.game.item.ids.*
import org.jesse.game.model.item.*

class ImbuedItemsItemaction : ItemActionScript() {

    val list = ImbueableItem.values
        .map { it.imbued }
        .filterNot {
            it == RING_OF_SUFFERING_I
                    || it == RING_OF_SUFFERING_RI
                    || it == SALVE_AMULET
                    || it == SALVE_AMULET_E
                    || it == SALVE_AMULETI
                    || it == SALVE_AMULETEI
                    || it == SALVE_AMULETI_25250
                    || it == SALVE_AMULETEI_25278
                    || it == SALVE_AMULETI_26763
                    || it == SALVE_AMULETEI_26782
        }
        .filter { ItemDefinitions.getOrThrow(it).containsOption("Uncharge") }

    init {
        /**
         * @author Kris | 12/06/2022
         */
        items(list)

        death {
            if (pvp) {
                val imbueable = ImbueableItem.get(item.id)
                lost { yield(Item(imbueable.normal)) }
                status { ItemDeathStatus.DROP_ON_DEATH }
            } else {
                kept { yield(item) }
                status { ItemDeathStatus.GO_TO_GRAVESTONE }
            }
        }
    }
}
