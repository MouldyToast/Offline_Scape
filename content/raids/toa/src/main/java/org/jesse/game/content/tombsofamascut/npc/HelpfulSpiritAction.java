package org.jesse.game.content.tombsofamascut.npc;

import org.jesse.game.GameInterface;
import org.jesse.game.content.tombsofamascut.encounter.MainHallEncounter;
import org.jesse.game.world.entity.npc.actions.NPCPlugin;

/**
 * @author Savions.
 */
public class HelpfulSpiritAction extends NPCPlugin {

	@Override public void handle() {
		bind("Claim", (player, npc) -> {
			if (!player.getTOAManager().isCanClaimSupplies()) {
				player.sendMessage("The spirit gives you a strange look. You've clearly claimed all you can for now.");
			} else {
				GameInterface.TOA_SUPPLIES_SHOP.open(player);
			}
		});
	}

	@Override public int[] getNPCs() {
		return new int[] {MainHallEncounter.HELPFUL_SPIRIT_ID};
	}
}
