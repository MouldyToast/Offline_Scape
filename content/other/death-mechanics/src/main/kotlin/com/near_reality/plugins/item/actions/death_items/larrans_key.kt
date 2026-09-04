package com.near_reality.plugins.item.actions.death_items

import com.zenyte.game.content.lootkeys.LootkeyConstants
import com.zenyte.game.model.item.pluginextensions.ItemDeathStatus
import com.near_reality.scripts.item.actions.ItemActionScript
import com.zenyte.game.item.ItemId
import com.zenyte.game.item.ItemId.*
import com.zenyte.game.model.item.*

class LarransKeyItemaction : ItemActionScript() {

    init {
        /**
         * @author Kris | 12/06/2022
         */
        items(LARRANS_KEY, *LootkeyConstants.LOOT_KEY_ORDER)

        death {
            setAlwaysLostOnDeath()
            if (pvp) {
                lost { yield(item) }
                status { ItemDeathStatus.DROP_ON_DEATH }
            } else {
                kept { yield(item) }
                status { ItemDeathStatus.GO_TO_GRAVESTONE }
            }
        }
    }
}
