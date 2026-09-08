package com.near_reality.plugins.item.actions.death_items

import com.zenyte.game.model.item.pluginextensions.ItemDeathStatus
import com.near_reality.scripts.item.actions.ItemActionScript
import com.zenyte.game.item.ItemId
import com.zenyte.game.model.item.*
import com.zenyte.game.item.ItemId.TORN_CLUE_SCROLL_PART_1
import com.zenyte.game.item.ItemId.TORN_CLUE_SCROLL_PART_2
import com.zenyte.game.item.ItemId.TORN_CLUE_SCROLL_PART_3

class TornClueScrollsItemaction : ItemActionScript() {

    init {
        /**
         * @author Kris | 12/06/2022
         */
        items(TORN_CLUE_SCROLL_PART_1, TORN_CLUE_SCROLL_PART_2, TORN_CLUE_SCROLL_PART_3)

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
