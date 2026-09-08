package org.jesse.game.content.tombsofamascut.npc;

import org.jesse.game.content.tombsofamascut.encounter.ScabarasEncounter;
import org.jesse.game.content.tombsofamascut.raid.ScabarasPuzzleType;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.npc.actions.NPCHandler;
import org.jesse.game.world.entity.npc.actions.NPCPlugin;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.action.combat.PlayerCombat;

/**
 * @author Savions.
 */
public class ScabarasObeliskAction extends NPCPlugin {

	@Override public void handle() {
		bind("Hit", new OptionHandler() {

			@Override public void click(Player player, NPC npc, NPCOption option) {
				player.stopAll();
				player.setFaceEntity(npc);
				handle(player, npc);
			}

			@Override public void handle(Player player, NPC npc) {
				PlayerCombat.attackEntity(player, npc, null);
			}
		});
	}

	@Override public int[] getNPCs() {
		return new int[] {ScabarasObelisk.ID};
	}
}
