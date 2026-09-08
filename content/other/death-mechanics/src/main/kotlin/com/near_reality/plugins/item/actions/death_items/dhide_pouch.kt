package com.near_reality.plugins.item.actions.death_items

import com.zenyte.game.model.item.pluginextensions.ItemDeathStatus
import com.near_reality.scripts.item.actions.ItemActionScript
import com.zenyte.game.item.ItemId
import com.zenyte.game.model.item.*

class DhidePouchItemaction : ItemActionScript() {

    init {
        items(26302, 26300)

        death {
            kept {
                yield(item)
            }
            status { ItemDeathStatus.KEEP_ON_DEATH }
        }
    }
}
