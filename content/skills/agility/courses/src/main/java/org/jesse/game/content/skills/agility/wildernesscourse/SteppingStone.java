package org.jesse.game.content.skills.agility.wildernesscourse;

import org.jesse.game.content.skills.agility.AgilityCourseObstacle;
import org.jesse.game.task.WorldTask;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.masks.ForceMovement;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Tommeh | 25 feb. 2018 : 17:41:20
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 *      profile</a>}
 */
public final class SteppingStone extends AgilityCourseObstacle {

	private static final Location START_LOC = new Location(3002, 3960, 0);

	public SteppingStone() {
		super(WildernessCourse.class, 3);
	}

	@Override
	public void startSuccess(final Player player, final WorldObject object) {
		player.lock();
		WorldTasksManager.schedule(new WorldTask() {
			int ticks;

			@Override
			public void run() {
				player.setAnimation(Animation.JUMP);
				player.setForceMovement(new ForceMovement(new Location(player.getX() - 1, player.getY(), player.getPlane()), 35, ForceMovement.WEST));
				player.addWalkSteps(player.getX() - 1, player.getY(), 2, false);
				if (ticks == 5) {
					player.unlock();
					stop();
				}
				ticks++;

			}
		}, 0, 1);
	}

	@Override
	public String getStartMessage(final boolean success) {
		return "You start crossing the stepping stones...";
	}

	@Override
	public Location getRouteEvent(final Player player, final WorldObject object) {
		return START_LOC;
	}

	@Override
	public int getLevel(final WorldObject object) {
		return 52;
	}

	@Override
	public int[] getObjectIds() {
		return new int[] { 23556 };
	}

	@Override
	public double getSuccessXp(final WorldObject object) {
		return 20;
	}

	@Override
	public int getDuration(final boolean success, final WorldObject object) {
		return 11;
	}

}
