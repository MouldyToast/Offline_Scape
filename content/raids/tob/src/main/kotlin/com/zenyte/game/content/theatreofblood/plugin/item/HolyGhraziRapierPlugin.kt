package com.zenyte.game.content.theatreofblood.plugin.item

import com.zenyte.game.item.Item
import com.zenyte.game.item.ids.*
import com.zenyte.game.model.item.pluginextensions.ItemPlugin
import com.zenyte.game.world.entity.player.Player

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