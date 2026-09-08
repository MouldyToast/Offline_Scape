package org.jesse.plugins.renewednpc;

import org.jesse.game.world.entity.npc.actions.NPCPlugin;

public class GethinNPC extends NPCPlugin {

	@Override
	public void handle() {
		bind("Trade", (player, npc) -> player.openShop("Premium Supplies"));
	}

	@Override
	public int[] getNPCs() {
		return new int[] { 16031 };
	}

}
