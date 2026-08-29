package com.zenyte.plugins.renewednpc;

import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.actions.NPCPlugin;

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
