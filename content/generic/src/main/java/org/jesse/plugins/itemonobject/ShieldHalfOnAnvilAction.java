package org.jesse.plugins.itemonobject;

import org.jesse.game.content.skills.smithing.DragonSqShieldCreationAction;
import org.jesse.game.item.Item;
import org.jesse.game.model.item.ItemOnObjectAction;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Tommeh | 21 jul. 2018 | 23:16:18
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class ShieldHalfOnAnvilAction implements ItemOnObjectAction {

	@Override
	public void handleItemOnObjectAction(Player player, Item item, int slot, WorldObject object) {
		player.getActionManager().setAction(new DragonSqShieldCreationAction());
	}

	@Override
	public Object[] getItems() {
		return new Object[] { 2366, 2368 };
	}

	@Override
	public Object[] getObjects() {
		return new Object[] { "Anvil" };
	}

}
