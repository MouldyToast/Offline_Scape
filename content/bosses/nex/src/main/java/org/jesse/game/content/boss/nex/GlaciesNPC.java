package org.jesse.game.content.boss.nex;

import org.jesse.game.util.Direction;
import org.jesse.game.world.entity.Location;
import org.jesse.game.npc.ids.NpcId;
import org.jesse.game.world.entity.player.action.combat.magic.CombatSpell;

public class GlaciesNPC extends NexMinion {

	public static final int ID = NpcId.GLACIES;

	protected GlaciesNPC(Location tile, Direction facing, NexNPC nex) {
		super(ID, tile, facing, nex);
	}

	@Override
	public void sendDeath() {
		super.sendDeath();

		NexNPC nex = getNex();
		if (nex != null) {
			nex.switchStage(NexStage.ZAROS_SWITCH);
		}
	}

	@Override
	public CombatSpell getSpell() {
		return CombatSpell.ICE_BARRAGE;
	}

}
