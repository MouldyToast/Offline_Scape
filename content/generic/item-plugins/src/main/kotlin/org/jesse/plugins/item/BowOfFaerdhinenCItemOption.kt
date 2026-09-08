package org.jesse.plugins.item

import org.jesse.game.content.crystal.recipes.chargeable.CrystalWeapon
import org.jesse.game.item.Item
import org.jesse.game.item.ids.*
import org.jesse.game.model.item.pluginextensions.ItemPlugin
import org.jesse.game.util.Colour
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.container.RequestResult
import org.jesse.game.world.entity.player.dialogue.dialogue
import org.jesse.game.world.entity.player.dialogue.options

class BowOfFaerdhinenCItemOption : ItemPlugin() {

	override fun handle() {
		bind("Uncharge") { player: Player, item: Item, slot: Int ->
			player.dialogue {
				plain("When you uncharge this you will receive the bow but ${Colour.RED.wrap("no crystal shards")}.")
				options("Are you sure you want to dismantle this?") {
					"Yes, dismantle it." {
						player.inventory.run {
							if (deleteItem(slot, item).result == RequestResult.SUCCESS) {
								addItems(Item(CrystalWeapon.BowOfFaerdhinen.inactiveId))
								player.sendMessage("You revert the ${item.name} back into its raw materials.")
							}
						}
					}
					"No, I want to keep it" {}
				}
			}
		}
	}

	override fun getItems(): IntArray {
		return intArrayOf(
			BOW_OF_FAERDHINEN_C_25884,
			BOW_OF_FAERDHINEN_C_25886,
			BOW_OF_FAERDHINEN_C_25888,
			BOW_OF_FAERDHINEN_C_25890,
			BOW_OF_FAERDHINEN_C_25892,
			BOW_OF_FAERDHINEN_C_25894,
			BOW_OF_FAERDHINEN_C_25896,
		)
	}

}