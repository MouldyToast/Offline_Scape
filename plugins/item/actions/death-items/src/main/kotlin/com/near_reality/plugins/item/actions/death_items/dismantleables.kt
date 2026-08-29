package com.near_reality.plugins.item.actions.death_items

import com.zenyte.game.item.Item
import com.zenyte.game.model.item.enums.DismantleableItem
import com.zenyte.game.model.item.pluginextensions.ItemDeathStatus
import com.near_reality.scripts.item.actions.ItemActionScript
import com.zenyte.game.item.ItemId
import com.zenyte.game.item.ItemId.*
import com.zenyte.game.model.item.*

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
