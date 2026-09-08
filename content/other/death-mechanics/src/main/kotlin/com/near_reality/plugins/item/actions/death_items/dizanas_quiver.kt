package com.near_reality.plugins.item.actions.death_items

import com.near_reality.game.world.entity.player.dizanasQuiverAmmo
import com.near_reality.game.world.entity.player.dizanasQuiverAmmoAmount
import com.zenyte.game.item.Item
import com.zenyte.game.model.item.pluginextensions.ItemDeathStatus
import com.near_reality.scripts.item.actions.ItemActionScript
import com.zenyte.game.item.ItemId
import com.zenyte.game.model.item.*

class DizanasQuiverItemaction : ItemActionScript() {

    private fun Item.reverted() : Item? {
        return when(this.id) {
            DIZANAS_QUIVER_UNCHARGED -> return Item(DIZANAS_QUIVER_BROKEN)
            DIZANAS_QUIVER -> return Item(DIZANAS_QUIVER_BROKEN)
            BLESSED_DIZANAS_QUIVER -> return Item(BLESSED_DIZANAS_QUIVER_BROKEN)
            DIZANAS_MAX_CAPE -> return Item(DIZANAS_MAX_CAPE_BROKEN)
            else -> null
        }
    }

    init {
        items(DIZANAS_QUIVER_UNCHARGED, DIZANAS_QUIVER, BLESSED_DIZANAS_QUIVER, DIZANAS_MAX_CAPE)

        death {
            if (!pvp) {
                kept {
                    yield(item)
                }
                status { ItemDeathStatus.KEEP_ON_DEATH }
            } else if (!deepWilderness) {
                lost {
                    yield(Item(995, 270_000))
                    if (item.charges > 0) {
                        yield(Item(SUNFIRE_SPLINTERS, item.charges))
                    }
                    val itemId: Int = player.dizanasQuiverAmmo
                    val itemAmount: Int = player.dizanasQuiverAmmoAmount
                    if (itemId != -1) {
                        yield(Item(itemId, itemAmount))
                    }
                }
                status { ItemDeathStatus.DELETE }
            } else {
                lost {
                    yield(Item(995, 9_600))
                    if (item.charges > 0) {
                        yield(Item(SUNFIRE_SPLINTERS, item.charges))
                    }
                }
                status { ItemDeathStatus.DELETE }
            }
        }
    }
}
