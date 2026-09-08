package org.jesse.plugins.itemonitem;

import org.jesse.game.item.Item;
import org.jesse.game.item.ids.ItemId;
import org.jesse.game.model.item.ItemOnItemAction;
import org.jesse.game.world.entity.player.Player;
import org.jesse.plugins.dialogue.ItemChat;

/**
 * @author Savions.
 */
public class AvernicDefenderItemOnItemAction implements ItemOnItemAction {

	@Override public void handleItemOnItemAction(Player player, Item from, Item to, int fromSlot, int toSlot) {
		final Item hilt = from.getId() == ItemId.AVERNIC_DEFENDER ? to : from;
		player.getInventory().deleteItem(from);
		player.getInventory().deleteItem(to);
		final int ghommalDefenderId = hilt.getId() == ItemId.GHOMMALS_HILT_5 ? ItemId.GHOMMALS_AVERNIC_DEFENDER_5 : ItemId.GHOMMALS_AVERNIC_DEFENDER_6;
		player.getInventory().addItem(new Item(ghommalDefenderId));
		player.getDialogueManager().start(new ItemChat(player, ghommalDefenderId, "You combine tgoether Ghommal's hilt and your avernic defender."));
	}

	@Override public int[] getItems() {
		return null;
	}

	@Override public ItemPair[] getMatchingPairs() {
		return new ItemPair[] {new ItemPair(ItemId.GHOMMALS_HILT_5, ItemId.AVERNIC_DEFENDER), new ItemPair(ItemId.GHOMMALS_HILT_6, ItemId.AVERNIC_DEFENDER)};
	}
}
