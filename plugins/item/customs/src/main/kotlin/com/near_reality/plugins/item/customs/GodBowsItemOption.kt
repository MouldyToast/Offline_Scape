package com.near_reality.plugins.item.customs

import com.near_reality.game.item.CustomItemId
import com.zenyte.game.item.Item
import com.zenyte.game.model.item.pluginextensions.ItemPlugin
import com.zenyte.game.world.entity.player.Player

class GodBowsItemOption : ItemPlugin() {

	override fun handle() {
		bind("Uncharge") { player: Player, _: Item, _: Int ->
			player.sendMessage("This seems pointless to do now.")
		}
	}

	override fun getItems(): IntArray {
		return intArrayOf(
			CustomItemId.ARMADYL_BOW,
			CustomItemId.BANDOS_BOW,
			CustomItemId.SARADOMIN_BOW,
			CustomItemId.ZAMORAK_BOW
		)
	}

}