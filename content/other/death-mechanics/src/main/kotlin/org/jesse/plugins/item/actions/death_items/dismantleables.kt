package org.jesse.plugins.item.actions.death_items

import org.jesse.game.item.Item
import org.jesse.game.model.item.enums.DismantleableItem
import org.jesse.game.model.item.pluginextensions.ItemDeathStatus
import org.jesse.scripts.item.actions.ItemActionScript
import org.jesse.game.item.ids.*
import org.jesse.game.model.item.*

class DismantleablesItemaction : ItemActionScript() {

    private val items = DismantleableItem.MAPPED_VALUES.filter { it.value.isSplitOnDeath }.map { it.key }

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
                val dismantleable = DismantleableItem.MAPPED_VALUES[item.id]
                val base = Item(dismantleable.baseItem)
                if(!base.isTradable) {
                    lost {
                        yield(Item(dismantleable.kit))
                    }
                    kept { yield(Item(dismantleable.baseItem))}
                    status { ItemDeathStatus.DROP_ON_DEATH }
                } else {
                    lost {
                        yield(Item(dismantleable.kit))
                        yield(Item(dismantleable.baseItem))
                    }
                    status { ItemDeathStatus.DROP_ON_DEATH }
                }
            }
        }
    }
}
