package org.jesse.game.content.skills.agility.varrockrooftop;

import org.jesse.game.content.skills.agility.AgilityCourseObstacle;
import org.jesse.game.content.skills.agility.MarkOfGrace;
import org.jesse.game.task.WorldTask;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.masks.ForceMovement;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;

public final class RoughWall extends AgilityCourseObstacle {
	
	private static final Animation JUMP = new Animation(2585);
	private static final Animation CLIMB = new Animation(828);
	
	private static final Location JUMP_SPOT = new Location(3220, 3414, 3);
	private static final Location LAND_SPOT = new Location(3219, 3414, 3);

	public RoughWall() {
		super(VarrockRooftopCourse.class, 1);
	}

	@Override
	public void startSuccess(Player player, WorldObject object) {
		WorldTasksManager.schedule(new WorldTask() {

			private int ticks;
			
			@Override
			public void run() {
				if(ticks == 0)
					player.setAnimation(CLIMB);
				else if(ticks == 1) {
					player.setLocation(JUMP_SPOT);
					player.setAnimation(JUMP);
				} else if(ticks == 2)
					player.setForceMovement(new ForceMovement(LAND_SPOT, 45, ForceMovement.WEST));
				else if(ticks == 3) {
					player.setLocation(LAND_SPOT);
					MarkOfGrace.spawn(player, VarrockRooftopCourse.MARK_LOCATIONS, 40, 30);
					stop();
				}
				ticks++;
			}
		}, 0, 0);
	}

	@Override
	public double getSuccessXp(WorldObject object) {
		return 12;
	}

	@Override
	public int getLevel(WorldObject object) {
		return 30;
	}

	@Override
	public int getDuration(boolean success, WorldObject object) {
		return 4;
	}
	
	@Override
	public int[] getObjectIds() {
		return new int[] {ObjectId.ROUGH_WALL_14412 };
	}

}
