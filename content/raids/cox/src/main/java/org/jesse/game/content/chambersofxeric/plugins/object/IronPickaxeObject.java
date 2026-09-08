package org.jesse.game.content.chambersofxeric.plugins.object;

import org.jesse.game.item.Item;
import org.jesse.game.item.ids.ItemId;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;

public class IronPickaxeObject implements ObjectAction {

	@Override
	public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
		player.getRaid().ifPresent(raid -> {
			if (!player.getInventory().hasFreeSlots()) {
				player.sendMessage("You need some free inventory space to take the tools.");
				return;
			}
			Item item = new Item(ItemId.IRON_PICKAXE);
			if (player.getInventory().containsItem(item)) {
				player.sendMessage("You have nothing more to take from here.");
				return;
			}
			player.getInventory().addOrDrop(item);
		});
	}

	@Override
	public Object[] getObjects() {
		return new Object[] { ObjectId.IRON_PICKAXE };
	}
}

