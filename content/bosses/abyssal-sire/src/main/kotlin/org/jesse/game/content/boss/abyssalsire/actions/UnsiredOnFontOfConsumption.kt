package org.jesse.game.content.boss.abyssalsire.actions

import org.jesse.game.content.boss.abyssalsire.AbyssalNexusArea
import org.jesse.game.content.boss.abyssalsire.AbyssalNexusCorner
import org.jesse.game.item.Item
import org.jesse.game.item.ids.*
import org.jesse.game.model.item.ItemOnObjectAction
import org.jesse.game.util.Utils
import org.jesse.game.world.World
import org.jesse.game.world.entity.masks.Animation
import org.jesse.game.world.entity.masks.Graphics
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.dialogue.Dialogue
import org.jesse.game.world.entity.player.dialogue.dialogue
import org.jesse.game.obj.ids.*
import org.jesse.game.world.`object`.WorldObject
import org.jesse.game.world.region.GlobalAreaManager

class UnsiredOnFontOfConsumption : ItemOnObjectAction {

	override fun handleItemOnObjectAction(player: Player, item: Item, slot: Int, `object`: WorldObject) {
		if (!player.inventory.containsItem(item)) {
			return
		}

		val lair = GlobalAreaManager[AbyssalNexusCorner.NORTH_EAST.areaName] as AbyssalNexusArea
		val size = lair.players.size
		if (size == 0) {
			player.dialogue { plain("There are no adventurers in this chamber.") }
		} else if (size == 1) {
			player.dialogue { plain("There is 1 adventurer in this chamber.") }
		} else {
			player.dialogue { plain("There are $size adventurers in this chamber.") }
		}

		player.lock(4)
		player.dialogueManager.start(object : Dialogue(player) {
			override fun buildDialogue() {
				item(Item(UNSIRED), "You place the Unsired into the Font of Consumption...", false)
			}
		})
		player.delay(2) {
			player.animation = Animation.LADDER_DOWN
			World.sendGraphics(
				SPLASH_GFX,
				player.location.transform(0, 1)
			)
			player.delay(0) {
				player.dialogueManager.start(object : Dialogue(player) {
					override fun buildDialogue() {
						val reward = rollItem(player)
						item(reward, "The Font consumes the Unsired and returns you a reward.")
						player.inventory.deleteItem(item)
						player.inventory.addOrDrop(reward)
						player.collectionLog.add(reward)
					}
				})
			}
		}
	}

	private fun rollItem(player: Player): Item {
		val roll = Utils.randomNoPlus(128)
		if (roll < 5) {
			return Item(ABYSSAL_ORPHAN)
		} else if (roll < 15) {
			return Item(ABYSSAL_HEAD)
		} else if (roll < 28) {
			return Item(JAR_OF_MIASMA)
		} else if (roll < 40) {
			return Item(ABYSSAL_WHIP)
		} else if (roll < 66) {
			return Item(ABYSSAL_DAGGER)
		} else {
			val containsClaw = player.containsItem(BLUDGEON_CLAW)
			return if (containsClaw && !player.containsItem(BLUDGEON_SPINE)) {
				Item(BLUDGEON_SPINE)
			} else if (containsClaw && !player.containsItem(BLUDGEON_AXON)) {
				Item(BLUDGEON_AXON)
			} else {
				Item(BLUDGEON_CLAW)
			}
		}
	}

	override fun getItems(): Array<Any> {
		return arrayOf(UNSIRED)
	}

	override fun getObjects(): Array<Any> {
		return arrayOf(THE_FONT_OF_CONSUMPTION)
	}

	companion object {
		private val SPLASH_GFX = Graphics(1276)
	}
}
