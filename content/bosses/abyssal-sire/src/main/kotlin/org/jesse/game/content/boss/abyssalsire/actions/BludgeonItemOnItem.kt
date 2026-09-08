package org.jesse.game.content.boss.abyssalsire.actions

import org.jesse.game.item.Item
import org.jesse.game.item.ids.*
import org.jesse.game.model.item.ItemOnItemAction
import org.jesse.game.world.entity.player.Player
import org.jesse.game.world.entity.player.dialogue.dialogue

class BludgeonItemOnItem : ItemOnItemAction {

	override fun handleItemOnItemAction(player: Player, from: Item, to: Item, fromSlot: Int, toSlot: Int) {
		if (!player.inventory.containsAll(bludgeonItems)) {
			player.dialogue { item(abyssalBludgeon, "You need Bludgeon Axon, Bludgeon Claw and Bludgeon Spine to combine into an Abyssal Bludgeon.") }
			return
		}

		player.inventory.deleteItems(bludgeonAxon, bludgeonClaw, bludgeonSpine)
		player.inventory.addItem(abyssalBludgeon)
		player.dialogue { item(abyssalBludgeon, "You combine Bludgeon Axon, Bludgeon Claw and Bludgeon Spine into an Abyssal Bludgeon.") }
	}

	override fun getItems(): IntArray? {
		return null
	}

	override fun getMatchingPairs(): Array<ItemOnItemAction.ItemPair> {
		return arrayOf(
			ItemOnItemAction.ItemPair(BLUDGEON_AXON, BLUDGEON_CLAW),
			ItemOnItemAction.ItemPair(BLUDGEON_AXON, BLUDGEON_SPINE),
			ItemOnItemAction.ItemPair(BLUDGEON_CLAW, BLUDGEON_SPINE),
			ItemOnItemAction.ItemPair(BLUDGEON_CLAW, BLUDGEON_AXON),
			ItemOnItemAction.ItemPair(BLUDGEON_SPINE, BLUDGEON_CLAW),
			ItemOnItemAction.ItemPair(BLUDGEON_SPINE, BLUDGEON_AXON),
		)
	}

	companion object {
		private val bludgeonAxon = Item(BLUDGEON_AXON)
		private val bludgeonClaw = Item(BLUDGEON_CLAW)
		private val bludgeonSpine = Item(BLUDGEON_SPINE)
		private val abyssalBludgeon = Item(ABYSSAL_BLUDGEON)
		private val bludgeonItems = mutableListOf(BLUDGEON_AXON, BLUDGEON_CLAW, BLUDGEON_SPINE)
	}

}