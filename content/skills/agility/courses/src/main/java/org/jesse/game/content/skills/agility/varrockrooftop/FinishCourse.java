package org.jesse.game.content.skills.agility.varrockrooftop;

import org.jesse.game.content.achievementdiary.diaries.VarrockDiary;
import org.jesse.game.content.skills.agility.AgilityCourseObstacle;
import org.jesse.game.content.skills.agility.MarkOfGrace;
import org.jesse.game.task.WorldTask;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.masks.ForceMovement;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.dailychallenge.challenge.SkillingChallenge;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;

public final class FinishCourse extends AgilityCourseObstacle {
	
	private static final Animation LAND = new Animation(2586);

	public FinishCourse() {
		super(VarrockRooftopCourse.class, 8);
	}

	@Override
	public void startSuccess(final Player player, final WorldObject object) {
		final Location ledge = new Location(player.getX(), player.getY()+1, 3);
		final Location finish = new Location(player.getX(), player.getY()+2, 0);
		player.setFaceLocation(ledge);
		WorldTasksManager.schedule(new WorldTask() {
			private int ticks;
			
			@Override
			public void run() {
				if(ticks == 0) {
					player.setAnimation(Animation.JUMP);
					player.setForceMovement(new ForceMovement(player.getLocation(), 15, ledge, 35, ForceMovement.NORTH));
				} else if(ticks == 1) {
					player.setLocation(ledge);
					player.setAnimation(LAND);
				} else if(ticks == 2) {
					player.getDailyChallengeManager().update(SkillingChallenge.COMPLETE_LAPS_VARROCK_COURSE);
					player.getAchievementDiaries().update(VarrockDiary.COMPLETE_AGILITY_COURSE_LAP);
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
	public int[] getObjectIds() {
		return new int[] {ObjectId.EDGE};
	}

	@Override
	public int getDuration(final boolean success, final WorldObject object) {
		return 3;
	}

	@Override
	public double getSuccessXp(final WorldObject object) {
		return 125;
	}
	
}
