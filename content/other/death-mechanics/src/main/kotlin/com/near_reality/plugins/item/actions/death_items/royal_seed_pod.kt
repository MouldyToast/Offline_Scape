package com.near_reality.plugins.item.actions.death_items

import com.zenyte.game.model.item.pluginextensions.ItemDeathStatus
import com.near_reality.scripts.item.actions.ItemActionScript
import com.zenyte.game.item.ItemId
import com.zenyte.game.model.item.*
import com.zenyte.game.item.ItemId.ROYAL_SEED_POD

class RoyalSeedPodItemaction : ItemActionScript() {

    init {
        /**
         * @author Kris | 11/06/2022
         */
        items(ROYAL_SEED_POD)

        death {
            kept {
                yield(item)
            }
            status {
                ItemDeathStatus.KEEP_ON_DEATH
            }
        }
    }
}
