package org.jesse.game.content.theatreofblood.plugin.item

import org.jesse.game.item.Item
import org.jesse.game.item.ids.*
import org.jesse.game.model.item.pluginextensions.ItemPlugin
import org.jesse.game.world.entity.player.Player

class HolyGhraziRapierPlugin : ItemPlugin() {

	override fun handle() {
		bind(
			"Dismantle"
		) { player: Player, item: Item, slotId: Int ->
			if (player.inventory.containsItem(item) && player.inventory.hasSpaceFor(HOLY_ORNAMENT_KIT, GHRAZI_RAPIER)) {
				player.inventory.deleteItem(item)
				player.inventory.addItem(Item(HOLY_ORNAMENT_KIT))
				player.inventory.addItem(Item(GHRAZI_RAPIER))
				player.sendMessage("You dismantle your rapier.")
			} else {
				player.sendMessage("Not enough space in your inventory.")
			}
		}
	}

	override fun getItems(): IntArray {
		return intArrayOf(
			HOLY_GHRAZI_RAPIER,
		)
	}

}