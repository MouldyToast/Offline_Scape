package org.jesse.plugins.item.actions.death_items

import org.jesse.game.item.Item
import org.jesse.game.model.item.pluginextensions.ItemDeathStatus
import org.jesse.scripts.item.actions.ItemActionScript
import org.jesse.game.item.ids.*
import org.jesse.game.model.item.*

class VolatileNightmareStaffItemaction : ItemActionScript() {

    init {
        items(VOLATILE_NIGHTMARE_STAFF)

        death {
            if (pvp) {
                lost {
                    yield(Item(VOLATILE_ORB))
                    yield(Item(NIGHTMARE_STAFF))
                }
                status { ItemDeathStatus.DROP_ON_DEATH }
            } else {
                kept { yield(item) }
                status { ItemDeathStatus.GO_TO_GRAVESTONE }
            }
        }
    }
}
