package org.jesse.plugins.itemonitem;

import org.jesse.game.item.Item;
import org.jesse.game.model.item.ItemOnItemAction;
import org.jesse.game.world.entity.player.Player;
import org.jesse.plugins.dialogue.skills.GlassBlowingD;

/**
 * @author Kris | 11. nov 2017 : 0:19.35
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 */
public final class GlassblowingItemAction implements ItemOnItemAction {

	@Override
	public void handleItemOnItemAction(final Player player, final Item from, final Item to, final int fromSlot, final int toSlot) {
		player.getDialogueManager().start(new GlassBlowingD(player));
	}

	@Override
	public int[] getItems() {
		return new int[] { 1775, 1785 };
	}

}