package org.jesse.plugins.item.actions.death_items

import org.jesse.game.model.item.pluginextensions.ItemDeathStatus
import org.jesse.scripts.item.actions.ItemActionScript
import org.jesse.game.item.ids.*
import org.jesse.game.model.item.*

class GemBagItemaction : ItemActionScript() {

    init {
        /**
         * The gem bag does not lose its contents, this is presumably due to it being able to carry up to 300 non-stacking
         * items, which would easily cause some overflows on death.
         * @author Kris | 12/06/2022
         */
        items(GEM_BAG_12020, OPEN_GEM_BAG)

        death {
        	kept { yield(item) }
        	status { ItemDeathStatus.KEEP_ON_DEATH }
        }
    }
}
