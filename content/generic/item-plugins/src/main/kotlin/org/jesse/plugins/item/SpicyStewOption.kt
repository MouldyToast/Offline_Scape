package org.jesse.plugins.item

import org.jesse.game.item.Item
import org.jesse.game.item.ids.*
import org.jesse.game.model.item.pluginextensions.ItemPlugin
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.player.Player

class SpicyStewOption: ItemPlugin() {

	override fun handle() {
		bind("Eat") { p: Player, _: Item, slot: Int ->
			if (p.variables.enhancedStewTick > 0) {
				p.sendMessage("You feel full from last stew.")
				return@bind
			}

			p.animation = Animation(829)
			p.inventory.replaceItem(BOWL, 1, slot)
			p.variables.enhancedStewTick = 200
			p.sendMessage("You eat the stew and feel energized.")
		}
	}

	override fun getItems(): IntArray {
		return intArrayOf(32159)
	}

}