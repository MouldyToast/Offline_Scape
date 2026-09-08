package org.jesse.game.content.tombsofamascut.object;

import org.jesse.game.content.tombsofamascut.encounter.CrondisPuzzleEncounter;
import org.jesse.game.item.Item;
import org.jesse.game.model.item.ItemOnNPCAction;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.npc.actions.NPCPlugin;
import org.jesse.game.world.entity.player.Player;

/**
 * @author Savions.
 */
public class PalmTreeAction extends NPCPlugin implements ItemOnNPCAction {

	@Override public void handle() {
		bind("Water", (player, npc) -> waterPalm(player));
	}

	@Override public int[] getNPCs() {
		return new int[] {CrondisPuzzleEncounter.PALM_NPC_ID, CrondisPuzzleEncounter.PALM_NPC_ID + 1,
				CrondisPuzzleEncounter.PALM_NPC_ID + 2, CrondisPuzzleEncounter.PALM_NPC_ID + 3};
	}

	@Override public void handleItemOnNPCAction(Player player, Item item, int slot, NPC npc) {
		waterPalm(player);
	}

	private void waterPalm(Player player) {
		if (player.getArea() instanceof CrondisPuzzleEncounter crondisPuzzleEncounter) {
			crondisPuzzleEncounter.waterPalm(player);
		}
	}

	@Override public Object[] getItems() {
		return new Object[] {CrondisPuzzleEncounter.CONTAINER_ITEM_ID};
	}

	@Override public Object[] getObjects() {
		return new Object[] {CrondisPuzzleEncounter.PALM_NPC_ID, CrondisPuzzleEncounter.PALM_NPC_ID + 1,
				CrondisPuzzleEncounter.PALM_NPC_ID + 2, CrondisPuzzleEncounter.PALM_NPC_ID + 3};
	}
}
