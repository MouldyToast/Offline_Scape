package org.jesse.plugins.item.actions.death_items

import org.jesse.game.item.Item
import org.jesse.game.model.item.pluginextensions.ItemDeathStatus
import org.jesse.scripts.item.actions.ItemActionScript
import org.jesse.game.item.ids.*
import org.jesse.game.model.item.*

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
