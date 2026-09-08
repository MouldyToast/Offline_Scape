package org.jesse.plugins.item.actions.death_items

import org.jesse.game.model.item.pluginextensions.ItemDeathStatus
import org.jesse.scripts.item.actions.ItemActionScript
import org.jesse.game.item.ids.*
import org.jesse.game.model.item.*

class RunePouchItemaction : ItemActionScript() {

    init {
        /**
         * @author Kris | 12/06/2022
         */
        items(RUNE_POUCH, RUNE_POUCH_L, DIVINE_RUNE_POUCH, DIVINE_RUNE_POUCH_L)

        death {
        	kept {
        		yield(item)
        		val runePouch = player.runePouch
        		for (rune in runePouch.container.items.values) {
        			if (rune == null) continue
        			yield(rune)
        		}
        	}
        	status {
        		ItemDeathStatus.KEEP_ON_DEATH
        	}
        }
    }
}
