package org.jesse.plugins.itemonitem;

import org.jesse.game.content.skills.crafting.CraftingDefinitions;
import org.jesse.game.item.Item;
import org.jesse.game.model.item.ItemOnItemAction;
import org.jesse.game.world.entity.player.Player;
import org.jesse.plugins.dialogue.PlayerChat;

/**
 * @author Tommeh | 26 aug. 2018 | 15:29:55
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public final class ThreadOnNeedleItemAction implements ItemOnItemAction {

	@Override
	public void handleItemOnItemAction(final Player player, final Item from, final Item to, final int fromSlot, final int toSlot) {
		player.getDialogueManager().start(new PlayerChat(player, "Perhaps I should use the needle with a piece of leather instead."));
	}

	@Override
	public int[] getItems() {
		return new int[] { CraftingDefinitions.THREAD.getId(), CraftingDefinitions.NEEDLE.getId() };
	}

}
