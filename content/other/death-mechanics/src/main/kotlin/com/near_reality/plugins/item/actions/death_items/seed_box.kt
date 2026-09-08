package com.near_reality.plugins.item.actions.death_items

import com.zenyte.game.model.item.pluginextensions.ItemDeathStatus
import com.near_reality.scripts.item.actions.ItemActionScript
import com.zenyte.game.item.ItemId
import com.zenyte.game.model.item.*

class SeedBoxItemaction : ItemActionScript() {

    init {
        /**
         * @author Kris | 12/06/2022
         */
        items(SEED_BOX, OPEN_SEED_BOX)

        death {
        	kept {
        		yield(item)
        	}
        	status { ItemDeathStatus.KEEP_ON_DEATH }
        }
    }
}
