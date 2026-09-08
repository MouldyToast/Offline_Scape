package com.near_reality.plugins.item.actions.death_items

import com.zenyte.game.item.Item
import com.zenyte.game.model.item.pluginextensions.ItemDeathStatus
import com.near_reality.scripts.item.actions.ItemActionScript
import com.zenyte.game.item.ItemId
import com.zenyte.game.model.item.*

class EldritchNightmareStaffItemaction : ItemActionScript() {

    init {
        items(ELDRITCH_NIGHTMARE_STAFF)

        death {
            if (pvp) {
                lost {
                    yield(Item(ELDRITCH_ORB))
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
