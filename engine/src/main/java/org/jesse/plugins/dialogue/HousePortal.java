package org.jesse.plugins.dialogue;

import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.dialogue.Dialogue;

public class HousePortal extends Dialogue {

	public HousePortal(Player player) {
		super(player);
	}

	@Override
	public void buildDialogue() {
		options(TITLE, "Go to your house", "Go to your house (building mode)", "Go to a friend's house", "Cancel").onOptionOne(() -> {
			player.getConstruction().enterHouse(false);
			finish();
		}).onOptionTwo(() -> {
			player.getConstruction().enterHouse(true);
			finish();
		}).onOptionThree(() -> {
			//TODO restrict ironmen from entering somebody else's house when the feature is added
			player.getDialogueManager().start(new PlainChat(player, "Currently disabled."));
		}).onOptionFour(() -> {
			finish();
		});
	}

}
