package com.zenyte.game.content.theatreofblood.plugin.item

import com.zenyte.game.item.Item
import com.zenyte.game.item.ids.*
import com.zenyte.game.model.item.ItemOnItemAction
import com.zenyte.game.world.entity.player.Player

class HolyOrnamentKit : ItemOnItemAction {

	enum class OrnamentData(val ornament: Int, val base: Int, val product: Int) {
		HOLY_SCYTHE(HOLY_ORNAMENT_KIT, SCYTHE_OF_VITUR_UNCHARGED, HOLY_SCYTHE_OF_VITUR_UNCHARGED),
		HOLY_RAPIER(HOLY_ORNAMENT_KIT, GHRAZI_RAPIER, HOLY_GHRAZI_RAPIER),
		HOLY_STAFF(HOLY_ORNAMENT_KIT, SANGUINESTI_STAFF_UNCHARGED, HOLY_SANGUINESTI_STAFF_UNCHARGED),
		SANGUINE_SCYTHE(SANGUINE_ORNAMENT_KIT, SCYTHE_OF_VITUR_UNCHARGED, SANGUINE_SCYTHE_OF_VITUR_UNCHARGED),
		;

		companion object {
			val VALUES: Array<OrnamentData> = OrnamentData.values()
		}
	}

	override fun handleItemOnItemAction(player: Player, from: Item, to: Item, fromSlot: Int, toSlot: Int) {
		var found: OrnamentData? = null
		for (value in OrnamentData.VALUES) {
			if (value.ornament == from.id && value.base == to.id || value.ornament == to.id && value.base == from.id) {
				found = value
				break
			}
		}

		if (found == null) {
			return
		}

		val inventory = player.inventory
		inventory.deleteItem(fromSlot, from)
		inventory.deleteItem(toSlot, to)
		inventory.addItem(Item(found.product))
		player.sendMessage("You add a kit to your item.")
	}

	override fun getItems(): IntArray? {
		return null
	}

	override fun getMatchingPairs(): Array<ItemOnItemAction.ItemPair> {
		return arrayOf(
			ItemOnItemAction.ItemPair(HOLY_ORNAMENT_KIT, SCYTHE_OF_VITUR_UNCHARGED),
			ItemOnItemAction.ItemPair(HOLY_ORNAMENT_KIT, GHRAZI_RAPIER),
			ItemOnItemAction.ItemPair(HOLY_ORNAMENT_KIT, SANGUINESTI_STAFF_UNCHARGED),
			ItemOnItemAction.ItemPair(SANGUINE_ORNAMENT_KIT, SCYTHE_OF_VITUR_UNCHARGED),
		)
	}

}