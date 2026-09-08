package org.jesse.plugins.renewednpc;

import org.jesse.game.GameInterface;
import org.jesse.game.content.ItemRetrievalService;
import org.jesse.game.npc.ids.NpcId;
import org.jesse.game.world.entity.npc.actions.NPCPlugin;
import org.jesse.game.world.entity.player.dialogue.Dialogue;

public class StrangeOldMan extends NPCPlugin {

	@Override
	public void handle() {
		bind("Talk-to", (player, npc) -> {
			if (player.getRetrievalService().getType() != ItemRetrievalService.RetrievalServiceType.ROTS || player.getRetrievalService().getContainer().isEmpty()) {
				player.getDialogueManager().start(new Dialogue(player, npc) {

					@Override
					public void buildDialogue() {
						npc("There's nothing to collect at this time.");
					}
				});
				return;
			}
			GameInterface.ITEM_RETRIEVAL_SERVICE.open(player);
		});
	}

	@Override
	public int[] getNPCs() {
		return new int[]{NpcId.STRANGE_OLD_MAN};
	}

}


