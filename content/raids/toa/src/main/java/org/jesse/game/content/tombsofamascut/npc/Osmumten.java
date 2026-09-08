package org.jesse.game.content.tombsofamascut.npc;

import org.jesse.game.world.entity.TargetSwitchCause;
import org.jesse.game.util.Direction;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.npc.NPC;

/**
 * @author Savions.
 */
public class Osmumten extends NPC {

	private static final Animation SPAWN_ANIM = new Animation(9795);

	public Osmumten(int id, Location tile, Direction facing) {
		super(id, tile, facing, 0);
		setAnimation(SPAWN_ANIM);
	}

	@Override public boolean addWalkStep(int nextX, int nextY, int lastX, int lastY, boolean check) { return false; }

	@Override public void setTarget(Entity target, TargetSwitchCause cause) { }
}
