package org.jesse.plugins.item.actions.death_items

import org.jesse.game.item.Item
import org.jesse.game.model.item.degradableitems.RepairableItem
import org.jesse.game.model.item.pluginextensions.ItemDeathStatus
import org.jesse.scripts.item.actions.ItemActionScript
import org.jesse.game.item.ids.*
import org.jesse.game.model.item.*

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
