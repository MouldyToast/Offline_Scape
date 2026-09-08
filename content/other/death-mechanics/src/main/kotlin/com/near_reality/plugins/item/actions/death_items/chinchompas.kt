package com.near_reality.plugins.item.actions.death_items

import com.zenyte.game.model.item.pluginextensions.ItemDeathStatus
import com.near_reality.scripts.item.actions.ItemActionScript
import com.zenyte.game.item.ItemId
import com.zenyte.game.model.item.*
import com.zenyte.game.item.ItemId.BLACK_CHINCHOMPA
import com.zenyte.game.item.ItemId.CHINCHOMPA_10033
import com.zenyte.game.item.ItemId.RED_CHINCHOMPA_10034

class ChinchompasItemaction : ItemActionScript() {

    init {
        /**
         * @author Kris | 12/06/2022
         */
        items(CHINCHOMPA_10033, RED_CHINCHOMPA_10034, BLACK_CHINCHOMPA)

        death {
            if (pvp) {
                lost { yield(item) }
                status { ItemDeathStatus.DROP_ON_DEATH }
            } else {
                status { ItemDeathStatus.DELETE }
            }
        }
    }
}
