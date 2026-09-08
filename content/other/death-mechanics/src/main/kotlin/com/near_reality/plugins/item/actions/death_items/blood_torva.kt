package com.near_reality.plugins.item.actions.death_items

import com.zenyte.game.item.Item
import com.zenyte.game.model.item.pluginextensions.ItemDeathStatus
import com.near_reality.scripts.item.actions.ItemActionScript
import com.zenyte.game.item.ItemId
import com.zenyte.game.model.item.*

class BloodTorvaItemaction : ItemActionScript() {

    private fun Item.reverted() : Item? {
        return when(this.id) {
            SANGUINE_TORVA_FULL_HELM -> return Item(TORVA_FULLHELM)
            SANGUINE_TORVA_PLATEBODY -> return Item(TORVA_PLATEBODY)
            SANGUINE_TORVA_PLATELEGS -> return Item(TORVA_PLATELEGS)
            else -> null
        }
    }

    init {
        items(SANGUINE_TORVA_FULL_HELM, SANGUINE_TORVA_PLATEBODY, SANGUINE_TORVA_PLATELEGS)

        death {
            if (!pvp) {
                kept {
                    yield(item)
                }
                status { ItemDeathStatus.KEEP_ON_DEATH }
            }
            else {
                lost {
                    item.reverted()?.let { yield(it) }
                }
                status { ItemDeathStatus.DROP_ON_DEATH }
            }
        }
    }
}
