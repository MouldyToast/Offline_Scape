package org.jesse.game.content.skills.agility.faladorrooftop;

import org.jesse.game.content.achievementdiary.diaries.FaladorDiary;
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
/**
 * @author Noele | May 1, 2018 : 1:37:39 AM
 * @see https://noeles.life || noele@zenyte.com
 */
public final class FinishCourse extends AgilityCourseObstacle {

	private static final Animation LEAP = new Animation(1603);
	private static final Animation JUMP = new Animation(2583);
	private static final Animation LAND = new Animation(2588);

	public FinishCourse() {
		super(FaladorRooftopCourse.class, 6);
	}

	@Override
	public void startSuccess(final Player player, final WorldObject object) {
		final Location ledge = new Location(player.getX()+3, player.getY(), 3);
		final Location finish = new Location(ledge.getX()+2, player.getY(), 0);
		player.setFaceLocation(finish);
		WorldTasksManager.schedule(new WorldTask() {

			private int ticks;
			
			@Override
			public void run() {
				if(ticks == 0) {
					player.setAnimation(LEAP);
					player.setForceMovement(new ForceMovement(ledge, 45, ForceMovement.EAST));
				} else if(ticks == 2) {
					player.setLocation(ledge);
				} else if(ticks == 3) {
					player.setAnimation(JUMP);
				} else if(ticks == 4) {
					player.getAchievementDiaries().update(FaladorDiary.COMPLETE_ROOFTOP_COURSE);
					player.setAnimation(LAND);
					player.setLocation(finish);
					MarkOfGrace.spawn(player, FaladorRooftopCourse.MARK_LOCATIONS, 50, 50);
					stop();
				}
				ticks++;
			}
			
		}, 0, 0);
	}
	
	@Override
	public int getLevel(final WorldObject object) {
		return 50;
	}

	@Override
	public int getDuration(final boolean success, final WorldObject object) {
		return 4;
	}

	@Override
	public double getSuccessXp(final WorldObject object) {
		return 180;
	}
	
	@Override
	public int[] getObjectIds() {
		return new int[] {ObjectId.EDGE_14925 };
	}
}
