package org.jesse.plugins.itemonobject;

import org.jesse.game.content.skills.smithing.SmeltableBar;
import org.jesse.game.item.Item;
import org.jesse.game.model.item.ItemOnObjectAction;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.WorldObject;
import org.jesse.plugins.dialogue.skills.SmeltingD;

import java.util.ArrayList;

/**
 * @author Kris | 11. nov 2017 : 0:46.21
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 */
public final class SmeltingFurnaceObjectAction implements ItemOnObjectAction {
	@Override
	public void handleItemOnObjectAction(Player player, Item item, int slot, WorldObject object) {
		player.getDialogueManager().start(new SmeltingD(player, object));
	}

	@Override
	public Object[] getItems() {
		final ArrayList<Object> list = new ArrayList<Object>();
		for (final SmeltableBar bar : SmeltableBar.VALUES) {
			for (final Item material : bar.getMaterials()) {
				list.add(material.getId());
			}
		}
		return list.toArray(new Object[list.size()]);
	}

	@Override
	public Object[] getObjects() {
		return new Object[] {"Furnace", "Clay forge", "Lava furnace"};
	}
}
