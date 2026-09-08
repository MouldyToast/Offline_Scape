package org.jesse.plugins.item.actions.death_items

import org.jesse.game.model.item.pluginextensions.ItemDeathStatus
import org.jesse.scripts.item.actions.ItemActionScript
import org.jesse.game.item.ids.*
import org.jesse.game.model.item.*

class BonePouchItemaction : ItemActionScript() {

    init {
        items(26306, 26304)

        death {
            kept {
                yield(item)
            }
            status { ItemDeathStatus.KEEP_ON_DEATH }
        }
    }
}
