/**
 * 
 */
package org.jesse.game.content.skills.agility.seersrooftop;

import org.jesse.game.content.skills.agility.AgilityCourseObstacle;
import org.jesse.game.content.skills.agility.MarkOfGrace;
import org.jesse.game.task.WorldTask;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Noele | May 1, 2018 : 2:58:00 AM
 */
public final class FirstJumpGap extends AgilityCourseObstacle {

	private static final Animation LEAP = new Animation(2586);

	private static final Location START = new Location(2721, 3494, 3);
	private static final Location LEDGE = new Location(2719, 3495, 2);
	private static final Location END = new Location(2713, 3494, 2);

	public FirstJumpGap() {
		super(SeersRooftopCourse.class, 2);
	}

	@Override
	public void startSuccess(final Player player, final WorldObject object) {
		player.faceObject(object);
		WorldTasksManager.schedule(new WorldTask() {
			private int ticks;
			
			@Override
			public void run() {
				if (ticks == 0)
					player.setAnimation(LEAP);
				else if (ticks == 1) {
					player.setLocation(LEDGE);
					player.setAnimation(Animation.LAND);
				} else if (ticks == 2)
					player.setAnimation(LEAP);
				else if (ticks == 3) {
					player.setAnimation(Animation.LAND);
					player.setLocation(END);
					MarkOfGrace.spawn(player, SeersRooftopCourse.MARK_LOCATIONS, 60, 20);
					stop();
				}
				ticks++;
			}
		}, 0, 0);
	}
	
	@Override
	public int getLevel(final WorldObject object) {
		return 60;
	}

	@Override
	public int[] getObjectIds() {
		return new int[] {ObjectId.GAP_14928 };
	}

	@Override
	public Location getRouteEvent(Player player, WorldObject object) {
		return START;
	}

	@Override
	public int getDuration(final boolean success, final WorldObject object) {
		return 4;
	}

	@Override
	public double getSuccessXp(final WorldObject object) {
		return 20;
	}
}
