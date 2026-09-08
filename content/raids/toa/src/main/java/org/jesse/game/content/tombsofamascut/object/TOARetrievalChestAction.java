package org.jesse.game.content.tombsofamascut.object;

import org.jesse.game.GameInterface;
import org.jesse.game.content.ItemRetrievalService;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.world.object.WorldObject;
import org.jesse.plugins.dialogue.PlainChat;

/**
 * @author Savions.
 */
public class TOARetrievalChestAction implements ObjectAction {

	@Override public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
		if (player.getRetrievalService().getType() != ItemRetrievalService.RetrievalServiceType.TOMBS_OF_AMASCUT || player.getRetrievalService().getContainer().isEmpty()) {
			player.getDialogueManager().start(new PlainChat(player, "There is nothing to collect."));
			return;
		}
		GameInterface.ITEM_RETRIEVAL_SERVICE.open(player);
	}

	@Override public Object[] getObjects() {
		return new Object[] {46078};
	}
}
