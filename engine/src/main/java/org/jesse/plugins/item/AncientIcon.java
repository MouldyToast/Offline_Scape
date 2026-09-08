package org.jesse.plugins.item;

import org.jesse.game.item.ids.ItemId;
import org.jesse.game.model.item.pluginextensions.ItemPlugin;
import org.jesse.plugins.dialogue.ItemChat;

/**
 * @author Savions.
 */
public class AncientIcon extends ItemPlugin {

	@Override public void handle() {
		bind("Inspect", (player, item, slotId) ->
			player.getDialogueManager().start(
				new ItemChat(player, item,
					"The icon has a strange magical aura about it. Perhaps I can use this with the Ancient Staff"))
		);
	}

	@Override public int[] getItems() {
		return new int[] {ItemId.ANCIENT_ICON};
	}
}
