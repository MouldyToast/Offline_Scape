package com.near_reality.plugins.item.actions.death_items

import com.zenyte.game.item.Item
import com.zenyte.game.model.item.enums.UpgradeKit
import com.zenyte.game.model.item.pluginextensions.ItemDeathStatus
import com.near_reality.scripts.item.actions.ItemActionScript
import com.zenyte.game.item.ItemId
import com.zenyte.game.item.ItemId.*
import com.zenyte.game.model.item.*

class UpgradablesItemaction : ItemActionScript() {

    private val items = UpgradeKit.MAPPED_VALUES.map { it.key }

    init {
        /**
         * @author Kris | 12/06/2022
         */
        items(items)

        death {
            if (!pvp) {
                kept { yield(item) }
                status { ItemDeathStatus.KEEP_ON_DEATH }
            } else {
                val dismantleable = UpgradeKit.MAPPED_VALUES[item.id]
                lost { yield(Item(dismantleable.baseItem)) }
                status { ItemDeathStatus.DROP_ON_DEATH }
            }
        }
    }
}
