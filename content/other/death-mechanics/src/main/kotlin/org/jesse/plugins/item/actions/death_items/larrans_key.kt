package org.jesse.plugins.item.actions.death_items

import org.jesse.game.content.lootkeys.LootkeyConstants
import org.jesse.game.model.item.pluginextensions.ItemDeathStatus
import org.jesse.scripts.item.actions.ItemActionScript
import org.jesse.game.item.ids.*
import org.jesse.game.model.item.*

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
