package org.jesse.plugins.itemonitem;

import org.jesse.game.content.skills.prayer.actions.Bones;
import org.jesse.game.item.Item;
import org.jesse.game.model.item.ItemOnItemAction;
import org.jesse.game.world.entity.player.Player;
import it.unimi.dsi.fastutil.ints.IntArrayList;


public class BonesOnBonePouchItemAction implements ItemOnItemAction {
	@Override
	public void handleItemOnItemAction(final Player player, final Item from, final Item to, final int fromSlot, final int toSlot) {
		final int boneSlot = (from.getId() != 26306 && from.getId() != 26304) ? fromSlot : toSlot;
		final int size = player.getBonePouch().getSize();
		if (size == 100) {
			player.sendMessage("You cannot more than 100 bones in your bone pouch.");
			return;
		}
		player.getBonePouch().getContainer().deposit(player, player.getInventory().getContainer(), boneSlot, 1);
		player.getInventory().refreshAll();
	}

	@Override
	public int[] getItems() {
		final IntArrayList list = new IntArrayList();
		for (final int id : Bones.BONES_MAP.keySet()) {
			list.add(id);
		}
		list.add(26306);
		list.add(26304);
		return list.toArray(new int[list.size()]);
	}
}
