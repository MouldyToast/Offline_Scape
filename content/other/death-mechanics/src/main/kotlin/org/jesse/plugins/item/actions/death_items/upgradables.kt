package org.jesse.plugins.item.actions.death_items

import org.jesse.game.item.Item
import org.jesse.game.model.item.enums.UpgradeKit
import org.jesse.game.model.item.pluginextensions.ItemDeathStatus
import org.jesse.scripts.item.actions.ItemActionScript
import org.jesse.game.item.ids.*
import org.jesse.game.model.item.*

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
