package org.jesse.game.content.boss.nightmare;

import org.jesse.game.world.entity.Location;
import org.jesse.game.npc.ids.NpcId;

public class NightmareTotemSE extends NightmareTotem {

	public static final int REGULAR = NpcId.TOTEM_9437;
	public static final int CHARGE = NpcId.TOTEM_9438;
	public static final int FULL_CHARGE = NpcId.TOTEM_9439;
	public static Location SPAWN_LOCATION = new Location(3879, 9942, 3);

	public NightmareTotemSE(BaseNightmareNPC boss, Location location) {
		super(REGULAR, location, boss);
	}

	@Override
	public int chargedId() {
		return FULL_CHARGE;
	}

	@Override
	public int activeId() {
		return CHARGE;
	}

	@Override
	public String chargedMessage() {
		return "The south east totem is fully charged.";
	}

}
