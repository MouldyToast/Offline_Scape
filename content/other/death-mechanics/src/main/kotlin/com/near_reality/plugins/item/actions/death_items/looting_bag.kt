package com.near_reality.plugins.item.actions.death_items

import com.zenyte.game.model.item.pluginextensions.ItemDeathStatus
import com.near_reality.scripts.item.actions.ItemActionScript
import com.zenyte.game.item.ItemId
import com.zenyte.game.model.item.*

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
