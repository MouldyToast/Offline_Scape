package org.jesse.plugins.item.actions.death_items

import org.jesse.game.item.Item
import org.jesse.game.model.item.pluginextensions.ItemDeathStatus
import org.jesse.scripts.item.actions.ItemActionScript
import org.jesse.game.item.ids.*
import org.jesse.game.model.item.*

class BowfaItemaction : ItemActionScript() {

    init {
        items(BOW_OF_FAERDHINEN, BOW_OF_FAERDHINEN_C, BOW_OF_FAERDHINEN_C_25884, BOW_OF_FAERDHINEN_C_25886,
        	BOW_OF_FAERDHINEN_C_25888, BOW_OF_FAERDHINEN_C_25890, BOW_OF_FAERDHINEN_C_25892, BOW_OF_FAERDHINEN_C_25894,
        	BOW_OF_FAERDHINEN_C_25896)

        death {
        	if (pvp) {
        		lost {
        			yield(Item(ENHANCED_CRYSTAL_WEAPON_SEED))
        		}
        		status { ItemDeathStatus.DROP_ON_DEATH }
        	} else {
        		lost { yield(item) }
        		status { ItemDeathStatus.GO_TO_GRAVESTONE }
        	}
        }
    }
}
