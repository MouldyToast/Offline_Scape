package com.near_reality.plugins.item.actions.death_items

import com.zenyte.game.item.Item
import com.zenyte.game.model.item.degradableitems.RepairableItem
import com.zenyte.game.model.item.pluginextensions.ItemDeathStatus
import com.near_reality.scripts.item.actions.ItemActionScript
import com.zenyte.game.item.ItemId
import com.zenyte.game.model.item.*
import com.zenyte.game.item.ItemId.COINS_995

class RepairableItemsItemaction : ItemActionScript() {

    val breakableItems = RepairableItem.VALUES.filterNot {
                it.isTradeable ||
                        it == RepairableItem.DIZANAS_QUIVER ||
                        it == RepairableItem.BLESSED_DIZANAS_QUIVER ||
                        it == RepairableItem.DIZANAS_MAX_CAPE
    }.map { it.ids.first() }

    init {
        /**
         * @author Kris | 12/06/2022
         */
        items(breakableItems)
        death {
            status {
                when {
                    pvp -> ItemDeathStatus.KEEP_DOWNGRADED
                    else -> ItemDeathStatus.GO_TO_GRAVESTONE
                }
            }
            if (pvp) {
                lost {
                    val receivedCoins = (item.definitions.price * 0.12).toInt()
                    if (receivedCoins > 0) {
                        yield(Item(COINS_995, receivedCoins))
                    }
                }
                kept {
                    yield(Item(RepairableItem.getItem(item).ids[1]))
                }
            } else {
                lost { yield(item) }
            }
        }
    }
}
