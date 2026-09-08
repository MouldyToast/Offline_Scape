package org.jesse.plugins.renewednpc;

import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.npc.actions.NPCPlugin;
import org.jesse.game.world.entity.npc.impl.vanstromklause.VanstromInstance;
import org.jesse.game.world.entity.player.dialogue.Dialogue;
import org.jesse.game.world.region.dynamicregion.AllocatedArea;
import org.jesse.game.world.region.dynamicregion.MapBuilder;
import org.jesse.logger.NearRealityPrintStream;

public class VanstromKlauseNpc extends NPCPlugin {

	@Override
	public void handle() {
		bind("Talk-to", (player, npc) -> player.getDialogueManager().start(new Dialogue(player, 3733) {
			@Override
			public void buildDialogue() {
				npc("Must I show you my true form? Begone you fool!");
				options("Fight Vanstrom Klause?", "Yes, I'll fight him.", "No thanks.")
						.onOptionOne(() -> {
							try {
								final AllocatedArea area = MapBuilder.findEmptyChunk(12, 12);
								VanstromInstance vanstromInstance = new VanstromInstance(area, player);
								vanstromInstance.constructRegion();
								player.setLocation(vanstromInstance.getLocation(new Location(3572, 3357)));
							} catch (Exception e) {
								e.printStackTrace(NearRealityPrintStream.getErrorStream());
							}
						});
			}
		}));
	}

	@Override
	public int[] getNPCs() {
		return new int[]{3733};
	}
}
