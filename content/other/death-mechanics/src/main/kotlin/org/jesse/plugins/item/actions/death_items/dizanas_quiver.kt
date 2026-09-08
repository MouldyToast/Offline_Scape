package org.jesse.plugins.item.actions.death_items

import org.jesse.game.world.entity.player.dizanasQuiverAmmo
import org.jesse.game.world.entity.player.dizanasQuiverAmmoAmount
import org.jesse.game.item.Item
import org.jesse.game.model.item.pluginextensions.ItemDeathStatus
import org.jesse.scripts.item.actions.ItemActionScript
import org.jesse.game.item.ids.*
import org.jesse.game.model.item.*

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
