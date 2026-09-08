package org.jesse.game.content.tombsofamascut.item;

import org.jesse.game.content.tombsofamascut.TOAManager;
import org.jesse.game.item.Item;
import org.jesse.game.item.ids.ItemId;
import org.jesse.game.model.item.ItemOnItemAction;
import org.jesse.game.world.entity.player.Player;

/**
 * @author Savions.
 */
public class SuppliesBagItemOnItemAction implements ItemOnItemAction {

	@Override public void handleItemOnItemAction(Player player, Item from, Item to, int fromSlot, int toSlot) {
		if (from == null || to == null) {
			return;
		}
		final boolean fromIsSupplies = from.getId() == ItemId.SUPPLIES;
		player.getTOAManager().storeSupply(fromIsSupplies ? toSlot : fromSlot, fromIsSupplies ? to : from);
	}

	@Override public int[] getItems() {
		final int[] items = new int[TOAManager.TOA_SUPPLIES_ITEM_IDS.length + 1];
		items[0] = ItemId.SUPPLIES;
		System.arraycopy(TOAManager.TOA_SUPPLIES_ITEM_IDS, 0, items, 1, items.length - 1);
		return items;
	}

	@Override public ItemPair[] getMatchingPairs() {
		final ItemPair[] itemPairs = new ItemPair[TOAManager.TOA_SUPPLIES_ITEM_IDS.length];
		for (int i = 0; i < itemPairs.length; i++) {
			itemPairs[i] = new ItemPair(ItemId.SUPPLIES, TOAManager.TOA_SUPPLIES_ITEM_IDS[i]);
		}
		return itemPairs;
	}
}
