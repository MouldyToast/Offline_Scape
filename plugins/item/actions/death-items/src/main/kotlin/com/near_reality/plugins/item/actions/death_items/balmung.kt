package com.near_reality.plugins.item.actions.death_items

import com.near_reality.game.item.CustomItemId
import com.zenyte.game.model.item.pluginextensions.ItemDeathStatus
import com.near_reality.scripts.item.actions.ItemActionScript
import com.zenyte.game.item.ItemId
import com.zenyte.game.item.ItemId.*
import com.zenyte.game.model.item.*

class BalmungItemaction : ItemActionScript() {

    init {
        /**
         * @author Leviticus | 01/26/2025
         */
        items(CustomItemId.BALMUNG)

        death {
            if (pvp) {
                lost { yield(item) }
                status { ItemDeathStatus.GO_TO_GRAVESTONE }
            } else {
                lost { yield(item) }
                status { ItemDeathStatus.GO_TO_GRAVESTONE }
            }
        }
    }
}
