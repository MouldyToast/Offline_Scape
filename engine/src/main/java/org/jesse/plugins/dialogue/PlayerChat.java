package org.jesse.plugins.dialogue;

import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.dialogue.Dialogue;

public class PlayerChat extends Dialogue {
	
	private final String message;

	public PlayerChat(Player player, String message) {
		super(player);
		this.message = message;
	}

	@Override
	public void buildDialogue() {
		player(message);
	}
}