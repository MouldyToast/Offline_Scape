package org.jesse.game.content.advent;

import org.jesse.game.util.Direction;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.npc.NPC;

public class SnowmanNPC extends NPC {

	private final int index;

	public SnowmanNPC(int id, Location tile, Direction facing, int index) {
		super(id, tile, facing, 0);
		this.index = index;
	}

	public int getEventIndex() {
		return index;
	}

}
