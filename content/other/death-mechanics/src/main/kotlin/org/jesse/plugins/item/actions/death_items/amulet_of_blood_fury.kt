package org.jesse.plugins.item.actions.death_items

import org.jesse.game.item.Item
import org.jesse.game.model.item.pluginextensions.ItemDeathStatus
import org.jesse.scripts.item.actions.ItemActionScript
import org.jesse.game.item.ids.*
import org.jesse.game.model.item.*

class AmuletOfBloodFuryItemaction : ItemActionScript() {

    init {
        items(AMULET_OF_BLOOD_FURY)

        death {
            if (pvp) {
                lost { yield(Item(AMULET_OF_FURY)) }
                status { ItemDeathStatus.DROP_ON_DEATH }
            } else {
                kept { yield(item) }
                status { ItemDeathStatus.GO_TO_GRAVESTONE }
            }
        }
    }
}
