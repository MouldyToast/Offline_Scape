package org.jesse.plugins.itemonobject;

import org.jesse.game.item.Item;
import org.jesse.game.model.item.ItemOnObjectAction;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.WorldObject;
import org.jesse.plugins.dialogue.skills.PotteryShapingD;

/**
 * @author Kris | 11. nov 2017 : 0:51.37
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 */
public final class PotteryShapingObjectAction implements ItemOnObjectAction {

	@Override
	public void handleItemOnObjectAction(Player player, Item item, int slot, WorldObject object) {
		player.getDialogueManager().start(new PotteryShapingD(player));
	}

	@Override
	public Object[] getItems() {
		return new Object[] { 1761 };
	}

	@Override
	public Object[] getObjects() {
		return new Object[] { "Potter's Wheel" };
	}

}
