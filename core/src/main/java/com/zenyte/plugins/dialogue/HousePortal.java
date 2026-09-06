package com.zenyte.plugins.dialogue;

import com.zenyte.game.content.skills.construction.ConstructionKeys;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

public class HousePortal extends Dialogue {

	public HousePortal(Player player) {
		super(player);
	}

	@Override
	public void buildDialogue() {
		options(TITLE, "Go to your house", "Go to your house (building mode)", "Go to a friend's house", "Cancel").onOptionOne(() -> {
			ConstructionKeys.construction(player).enterHouse(false);
			finish();
		}).onOptionTwo(() -> {
			ConstructionKeys.construction(player).enterHouse(true);
			finish();
		}).onOptionThree(() -> {
			//TODO restrict ironmen from entering somebody else's house when the feature is added
			player.getDialogueManager().start(new PlainChat(player, "Currently disabled."));
		}).onOptionFour(() -> {
			finish();
		});
	}

}
