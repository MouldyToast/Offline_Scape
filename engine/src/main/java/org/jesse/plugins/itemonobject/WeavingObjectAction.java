package org.jesse.plugins.itemonobject;

import org.jesse.game.content.skills.crafting.CraftingDefinitions.WeavingData;
import org.jesse.game.item.Item;
import org.jesse.game.model.item.ItemOnObjectAction;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.WorldObject;
import org.jesse.plugins.dialogue.skills.WeavingD;

import java.util.ArrayList;

/**
 * @author Kris | 11. nov 2017 : 0:49.48
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 */
public final class WeavingObjectAction implements ItemOnObjectAction {
	@Override
	public void handleItemOnObjectAction(Player player, Item item, int slot, WorldObject object) {
		player.getDialogueManager().start(new WeavingD(player));
	}

	@Override
	public Object[] getItems() {
		final ArrayList<Object> list = new ArrayList<Object>();
		for (WeavingData data : WeavingData.VALUES_ARR) {
			list.add(data.getMaterial().getId());
		}
		return list.toArray(new Object[list.size()]);
	}

	@Override
	public Object[] getObjects() {
		return new Object[] {"Loom"};
	}
}
