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
 * @author Noele | May 1, 2018 : 2:40:50 AM
 * @see https://noeles.life || noele@zenyte.com
 */
public final class StartCourse extends AgilityCourseObstacle {
	
	private static final Animation CLIMB = new Animation(737);
	private static final Animation HANG = new Animation(1118);
	
	private static final Location MAILBOX = new Location(2729, 3488, 1);
	private static final Location FINISH = new Location(2729, 3491, 3);

	public StartCourse() {
		super(SeersRooftopCourse.class, 1);
	}

	@Override
	public void startSuccess(final Player player, final WorldObject object) {
		player.faceObject(object);
		WorldTasksManager.schedule(new WorldTask() {

			private int ticks;
			
			@Override
			public void run() {
				if(ticks == 0)
					player.setAnimation(CLIMB);
				else if(ticks == 1) {
					player.setAnimation(HANG);
					player.setLocation(MAILBOX);
					player.sendFilteredMessage("...jump, and grab hold of the sign!");
				} else if(ticks == 3) {
					player.setAnimation(Animation.STOP);
					player.setLocation(FINISH);
					player.addAttribute("SeersTrapdoor", 0);
					MarkOfGrace.spawn(player, SeersRooftopCourse.MARK_LOCATIONS, 60, 20);
					stop();
				}
				ticks++;
			}
		}, 0, 0);
	}

	@Override
	public String getFilterableStartMessage(final boolean success) {
		return "You climb up the wall...";
	}
	
	@Override
	public int getLevel(final WorldObject object) {
		return 60;
	}

	@Override
	public int getDuration(final boolean success, final WorldObject object) {
		return 4;
	}

	@Override
	public double getSuccessXp(final WorldObject object) {
		return 45;
	}
	
	@Override
	public int[] getObjectIds() {
		return new int[] {ObjectId.WALL_14927 };
	}
}
