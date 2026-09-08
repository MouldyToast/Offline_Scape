package org.jesse.plugins.item.actions.death_items

import org.jesse.game.model.item.pluginextensions.ItemDeathStatus
import org.jesse.scripts.item.actions.ItemActionScript
import org.jesse.game.item.ids.*
import org.jesse.game.model.item.*

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
