package org.jesse.plugins.dialogue;

import org.jesse.game.item.Item;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.dialogue.Dialogue;

public class ItemChat extends Dialogue {
	
	private final String message;
	private final int itemId;

	public ItemChat(Player player, Item item, String message) {
		this(player, item.getId(), message);
	}

	public ItemChat(Player player, int itemId, String message) {
		super(player);
		this.message = message;
		this.itemId = itemId;
	}

	@Override
	public void buildDialogue() {
		item(itemId, message);
	}
}