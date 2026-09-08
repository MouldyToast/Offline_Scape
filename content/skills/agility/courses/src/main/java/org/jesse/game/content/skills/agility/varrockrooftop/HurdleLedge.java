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

public final class HurdleLedge extends AgilityCourseObstacle {
	
	private static final Animation JUMP = new Animation(1603);

	public HurdleLedge() {
		super(VarrockRooftopCourse.class, 7);
	}

	@Override
	public void startSuccess(final Player player, final WorldObject object) {
		final Location finish = new Location(player.getX(), player.getY()+2, 3);
		player.setFaceLocation(finish);
		WorldTasksManager.schedule(new WorldTask() {
			private int ticks;
			
			@Override
			public void run() {
				if(ticks == 0) {
					player.setAnimation(JUMP);
					player.setForceMovement(new ForceMovement(player.getLocation(), 15, finish, 35, ForceMovement.NORTH));
				} else if(ticks == 1) {
					player.setLocation(finish);
					MarkOfGrace.spawn(player, VarrockRooftopCourse.MARK_LOCATIONS, 40, 30);
					stop();
				}
				ticks++;
			}
		}, 0, 0);
	}
	
	@Override
	public int getLevel(final WorldObject object) {
		return 30;
	}

	@Override
	public int getDuration(final boolean success, final WorldObject object) {
		return 2;
	}

	@Override
	public double getSuccessXp(final WorldObject object) {
		return 3;
	}
	
	@Override
	public int[] getObjectIds() {
		return new int[] {ObjectId.LEDGE_14836 };
	}
}
