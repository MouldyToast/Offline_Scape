package org.jesse.plugins.itemonobject;

import org.jesse.game.content.skills.smithing.CannonballSmithing;
import org.jesse.game.item.Item;
import org.jesse.game.model.item.ItemOnObjectAction;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.WorldObject;
import org.jesse.plugins.dialogue.skills.CannonballSmithingD;

/**
 * @author Tommeh | 10 jun. 2018 | 16:25:25
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public final class SteelBarItemOnObjectAction implements ItemOnObjectAction {

	@Override
	public void handleItemOnObjectAction(Player player, Item item, int slot, WorldObject object) {
		if (!player.getInventory().containsItem(CannonballSmithing.MOULD)
				&& !player.getInventory().containsItem(CannonballSmithing.DOUBLE_AMMO_MOULD)) {
			player.sendMessage("You need a cannonball mould to make cannonballs.");
			return;
		}
		player.getDialogueManager().start(new CannonballSmithingD(player));
	}

	@Override
	public Object[] getItems() {
		return new Object[] { CannonballSmithing.MATERIAL.getId() };
	}

	@Override
	public Object[] getObjects() {
		return new Object[] { "Furnace" };
	}

}
