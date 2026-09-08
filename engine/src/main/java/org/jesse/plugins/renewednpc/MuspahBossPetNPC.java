package org.jesse.plugins.renewednpc;

import org.jesse.game.content.follower.Follower;
import org.jesse.game.content.follower.impl.BossPet;
import org.jesse.game.npc.ids.NpcId;

import java.util.Objects;

/**
 * @author Savions.
 */
public class MuspahBossPetNPC extends BossPetNPC {

	@Override public void handle() {
		bind("Metamorphosis", (player, npc) -> {
			if (!(npc instanceof Follower) || !Objects.equals(((Follower) npc).getOwner(), player)) {
				player.sendMessage("This is not your pet.");
				return;
			}
			if (player.getNumericAttribute("Muspah charged ice").intValue() <= 0) {
				player.sendMessage("You haven't unlocked the metamorphosis ability yet.");
				return;
			}
			final int target = BossPet.metamorphosisMap.getOrDefault(npc.getId(), -1);
			if (target == -1) {
				player.sendMessage("Nothing interesting happens.");
				return;
			}
			npc.setTransformation(target);
			player.setPetId(target);
		});
	}

	@Override public int[] getNPCs() {
		return new int[] { NpcId.MUPHIN, NpcId.MUPHIN_12006, NpcId.MUPHIN_12007 };
	}
}
