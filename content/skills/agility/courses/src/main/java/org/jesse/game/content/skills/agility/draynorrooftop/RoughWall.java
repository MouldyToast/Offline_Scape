package org.jesse.game.content.skills.agility.draynorrooftop;

import org.jesse.game.content.skills.agility.AgilityCourseObstacle;
import org.jesse.game.content.skills.agility.MarkOfGrace;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Tommeh | 25 feb. 2018 : 20:15:55
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public final class RoughWall extends AgilityCourseObstacle {
	
	private static final Location END_LOC = new Location(3102, 3279, 3);

	public RoughWall() {
		super(DraynorRooftopCourse.class, 1);
	}

	@Override
	public void startSuccess(final Player player, final WorldObject object) {
		MarkOfGrace.spawn(player, DraynorRooftopCourse.MARK_LOCATIONS, 40, 10);
		player.useStairs(828, END_LOC, 1, 1);
	}

	@Override
	public int getLevel(final WorldObject object) {
		return 10;
	}

	@Override
	public int[] getObjectIds() {
		return new int[] {ObjectId.ROUGH_WALL };
	}

	@Override
	public double getSuccessXp(final WorldObject object) {
		return 5;
	}

	@Override
	public int getDuration(final boolean success, final WorldObject object) {
		return 1;
	}

}
