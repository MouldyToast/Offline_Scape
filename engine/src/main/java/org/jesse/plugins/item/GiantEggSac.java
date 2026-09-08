package org.jesse.plugins.item;

import org.jesse.game.item.Item;
import org.jesse.game.item.ids.ItemId;
import org.jesse.game.model.item.pluginextensions.ItemPlugin;

public class GiantEggSac extends ItemPlugin {

	@Override
	public void handle() {
		bind("Cut-open", (player, item, inventorySlot) -> {
			if (!player.getInventory().containsItem(ItemId.KNIFE)) {
				player.sendMessage("You need a knife to cut open the sac.");
				return;
			}

			player.getInventory().deleteItem(item);
			player.getInventory().addItem(new Item(ItemId.RED_SPIDERS_EGGS + 1, 100));
			player.sendMessage("You cut open the sack and receive 100 eggs.");
		});
		bind("Remove-eggs", (player, item, inventorySlot) -> {
			if (!player.getInventory().containsItem(ItemId.KNIFE)) {
				player.sendMessage("You need a knife to cut open the sac.");
				return;
			}

			player.getInventory().deleteItem(item);
			player.getInventory().addItem(new Item(ItemId.RED_SPIDERS_EGGS + 1, 100));
			player.sendMessage("You cut open the sack and receive 100 eggs.");
		});
	}

	@Override
	public int[] getItems() {
		return new int[]{ItemId.GIANT_EGG_SAC,ItemId.GIANT_EGG_SACFULL};
	}

}
