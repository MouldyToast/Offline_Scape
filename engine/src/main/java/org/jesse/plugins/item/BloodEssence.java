package org.jesse.plugins.item;

import org.jesse.game.item.Item;
import org.jesse.game.item.ids.ItemId;
import org.jesse.game.model.item.pluginextensions.ItemPlugin;

/**
 * @author Savions.
 */
public class BloodEssence extends ItemPlugin {

	public static final int CHARGES = 1_000;

	@Override public void handle() {
		bind("activate", (player, item, container, slotId) -> {
			if(item.getId() == ItemId.BLOOD_ESSENCE && item.getAmount() < 1_000_000) {
				player.getInventory().deleteItem(slotId, item);
				player.getInventory().addItem(new Item(ItemId.BLOOD_ESSENCE_ACTIVE, 1, CHARGES * item.getAmount()));
				player.sendMessage("You activate the blood essence.");
			} else {
				player.sendMessage("You cannot activate this many blood essence at once.");
			}
		});
		bind("check", (player, item, container, slotId) -> {
			player.sendMessage("Your blood essence has " + item.getCharges() + " charges remaining.");
		});
	}

	@Override public int[] getItems() {
		return new int[] { ItemId.BLOOD_ESSENCE, ItemId.BLOOD_ESSENCE_ACTIVE };
	}
}
