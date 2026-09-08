package com.near_reality.plugins.item.actions.death_items

import com.zenyte.game.item.Item
import com.zenyte.game.model.item.pluginextensions.ItemDeathStatus
import com.near_reality.scripts.item.actions.ItemActionScript
import com.zenyte.game.item.ItemId
import com.zenyte.game.model.item.*

class FerociousGlovesItemaction : ItemActionScript() {

    init {
        /**
         * @author Kris | 12/06/2022
         */
        items(FEROCIOUS_GLOVES)

        death {
            if (pvp) {
                lost { yield(Item(HYDRA_LEATHER)) }
                status { ItemDeathStatus.DROP_ON_DEATH }
            } else {
                lost { yield(item) }
                status { ItemDeathStatus.GO_TO_GRAVESTONE }
            }
        }
    }
}
