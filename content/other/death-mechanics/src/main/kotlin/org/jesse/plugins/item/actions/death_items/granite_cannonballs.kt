package org.jesse.plugins.item.actions.death_items

import org.jesse.game.item.Item
import org.jesse.game.model.item.pluginextensions.ItemDeathStatus
import org.jesse.scripts.item.actions.ItemActionScript
import org.jesse.game.item.ids.*
import org.jesse.game.model.item.*

class GraniteCannonballsItemaction : ItemActionScript() {

    init {
        /**
         * @author Kris | 12/06/2022
         */
        items(GRANITE_CANNONBALL)

        death {
            if (pvp) {
                lost { yield(Item(STEEL_CANNONBALL, item.amount)) }
            } else {
                kept { yield(item) }
            }
            status {
                if (pvp) ItemDeathStatus.DROP_ON_DEATH else ItemDeathStatus.GO_TO_GRAVESTONE
            }
        }
    }
}
