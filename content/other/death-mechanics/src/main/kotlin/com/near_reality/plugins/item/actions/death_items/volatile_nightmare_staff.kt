package com.near_reality.plugins.item.actions.death_items

import com.zenyte.game.item.Item
import com.zenyte.game.model.item.pluginextensions.ItemDeathStatus
import com.near_reality.scripts.item.actions.ItemActionScript
import com.zenyte.game.item.ItemId
import com.zenyte.game.model.item.*
import com.zenyte.game.item.ItemId.NIGHTMARE_STAFF
import com.zenyte.game.item.ItemId.VOLATILE_NIGHTMARE_STAFF
import com.zenyte.game.item.ItemId.VOLATILE_ORB

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
