package org.jesse.plugins.renewednpc;

import org.jesse.game.npc.ids.NpcId;
import org.jesse.game.world.entity.npc.actions.NPCPlugin;

public class SigmundTheMerchantNPC extends NPCPlugin {

	@Override
	public void handle() {
		bind("Trade", (player, npc) -> player.openShop("Sigmund the merchant"));
	}

	@Override
	public int[] getNPCs() {
		return new int[] {NpcId.SIGMUND_THE_MERCHANT };
	}

}
