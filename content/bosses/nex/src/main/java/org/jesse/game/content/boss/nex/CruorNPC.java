package org.jesse.game.content.boss.nex;

import org.jesse.game.util.Direction;
import org.jesse.game.world.entity.Location;
import org.jesse.game.npc.ids.NpcId;
import org.jesse.game.world.entity.player.action.combat.magic.CombatSpell;

public class CruorNPC extends NexMinion {

	public static final int ID = NpcId.CRUOR;

	protected CruorNPC(Location tile, Direction facing, NexNPC nex) {
		super(ID, tile, facing, nex);
	}

	@Override
	public void sendDeath() {
		super.sendDeath();

		NexNPC nex = getNex();
		if (nex != null) {
			nex.clearReavers();
			nex.switchStage(NexStage.ICE_SWITCH);
		}
	}

	@Override
	public CombatSpell getSpell() {
		return CombatSpell.BLOOD_BARRAGE;
	}

}

