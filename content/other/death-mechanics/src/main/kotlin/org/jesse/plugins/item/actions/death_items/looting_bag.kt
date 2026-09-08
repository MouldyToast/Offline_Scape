package org.jesse.plugins.item.actions.death_items

import org.jesse.game.model.item.pluginextensions.ItemDeathStatus
import org.jesse.scripts.item.actions.ItemActionScript
import org.jesse.game.item.ids.*
import org.jesse.game.model.item.*

class LootingBagItemaction : ItemActionScript() {

    init {
        /**
         * @author Kris | 12/06/2022
         */
        items(LOOTING_BAG, LOOTING_BAG_22586)

        death {
            setAlwaysLostOnDeath()
            lost { yieldAll(player.lootingBag.container.items.values) }
            status { ItemDeathStatus.DELETE }
            afterDeath { player.lootingBag.clear() }
        }
    }
}
