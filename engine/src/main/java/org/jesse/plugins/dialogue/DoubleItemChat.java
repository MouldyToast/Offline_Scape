package org.jesse.plugins.dialogue;

import org.jesse.game.item.Item;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.dialogue.Dialogue;

public class DoubleItemChat extends Dialogue {
	
	private final String message;
	private final Item first;
    private final Item second;

	public DoubleItemChat(Player player, Item first, Item second, String message) {
		super(player);
		this.message = message;
		this.first = first;
		this.second = second;
	}

	@Override
	public void buildDialogue() {
		doubleItem(first, second, message);
	}
}