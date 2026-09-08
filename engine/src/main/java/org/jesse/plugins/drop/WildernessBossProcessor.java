package org.jesse.plugins.drop;

import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.npc.ids.NpcId;
import org.jesse.game.world.entity.npc.drop.matrix.DropProcessor;
import org.jesse.game.world.entity.player.Player;

public class WildernessBossProcessor extends DropProcessor {
	@Override
	public void attach() {
		//appendDrop(new DisplayedDrop(ItemID.BLOOD_MONEY, 1, 3, 1));
	}

	@Override
	public void onDeath(NPC npc, Player killer) {
		//npc.dropItem(killer, new Item(ItemID.BLOOD_MONEY, Utils.random(1, 3)));
	}

	@Override
	public int[] ids() {
		return new int[] {
				NpcId.CALLISTO, NpcId.SCORPIA, NpcId.CHAOS_FANATIC, NpcId.CRAZY_ARCHAEOLOGIST, NpcId.VETION, NpcId.CHAOS_ELEMENTAL,
				NpcId.VENENATIS
		};
	}
}