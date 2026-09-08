package org.jesse.plugins.renewednpc;

import org.jesse.game.world.entity.npc.actions.NPCPlugin;

public class PrimulaNPC extends NPCPlugin {

	@Override
	public void handle() {
		bind("Trade", (player, npc) -> player.openShop("Herblore Store"));
	}

	@Override
	public int[] getNPCs() {
		return new int[] { 16034 };
	}

}
