package com.near_reality.plugins.item.actions.death_items

import com.zenyte.game.model.item.pluginextensions.ItemDeathStatus
import com.near_reality.scripts.item.actions.ItemActionScript
import com.zenyte.game.item.ItemId
import com.zenyte.game.item.ItemId.*
import com.zenyte.game.model.item.*

class ImbuedCapesItemaction : ItemActionScript() {

    init {
        /**
         * @author Stan van der Bend
         */
        items(
            IMBUED_ANCIENT_CAPE,
            IMBUED_ARMADYL_CAPE,
            IMBUED_BANDOS_CAPE,
            IMBUED_SEREN_CAPE
        )

        death {
            setAlwaysKeptOnDeath()
            kept { yield(item) }
            status { ItemDeathStatus.KEEP_ON_DEATH }
        }
    }
}
