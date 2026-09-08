package com.near_reality.plugins.item.actions.death_items

import com.zenyte.game.item.Item
import com.zenyte.game.model.item.pluginextensions.ItemDeathStatus
import com.near_reality.scripts.item.actions.ItemActionScript
import com.zenyte.game.item.ItemId
import com.zenyte.game.model.item.*

class NeitiznotFaceguardItemaction : ItemActionScript() {

    init {
        items(NEITIZNOT_FACEGUARD)

        death {
        	if (pvp) {
        		lost {
        			yield(Item(HELM_OF_NEITIZNOT))
        			yield(Item(BASILISK_JAW))
        		}
        		status { ItemDeathStatus.DROP_ON_DEATH }
        	} else {
        		lost { yield(item) }
        		status { ItemDeathStatus.GO_TO_GRAVESTONE }
        	}
        }
    }
}
