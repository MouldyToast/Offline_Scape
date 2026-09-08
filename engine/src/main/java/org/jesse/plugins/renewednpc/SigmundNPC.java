package org.jesse.plugins.renewednpc;

import org.jesse.game.world.entity.npc.actions.NPCPlugin;

public class SigmundNPC extends NPCPlugin {

	@Override
	public void handle() {
		bind("Trade", (player, npc) -> player.openShop("Respected Premium Supplies"));
	}

	@Override
	public int[] getNPCs() {
		return new int[] { 16032 };
	}

}
