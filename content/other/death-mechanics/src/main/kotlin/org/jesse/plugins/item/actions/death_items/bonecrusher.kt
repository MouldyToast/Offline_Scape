package org.jesse.plugins.item.actions.death_items

import org.jesse.game.model.item.pluginextensions.ItemDeathStatus
import org.jesse.scripts.item.actions.ItemActionScript
import org.jesse.game.item.ids.*
import org.jesse.game.model.item.*

class BonecrusherItemaction : ItemActionScript() {

    init {
        /**
         * @author Kris | 12/06/2022
         */
        items(BONECRUSHER)

        death {
            if (!deepWilderness) {
                kept {
                    yield(item)
                }
                status { ItemDeathStatus.KEEP_ON_DEATH }
            } else {
                status { ItemDeathStatus.DELETE }
            }
        }
    }
}
