package org.jesse.game.content.tombsofamascut.object;

import org.jesse.game.content.skills.agility.Shortcut;
import org.jesse.game.content.tombsofamascut.encounter.ScabarasEncounter;
import org.jesse.game.task.WorldTask;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.SoundEffect;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.masks.ForceMovement;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Savions.
 */
public class SteppingStonesAction implements Shortcut {

	private static final Location NORTH_LOCATION = new Location(3560, 5283);
	private static final Location SOUTH_LOCATION = new Location(3560, 5277);
	private static final SoundEffect JUMP_FIRST_SOUND = new SoundEffect(2461, 1, 20);
	private static final SoundEffect JUMP_SECOND_SOUND = new SoundEffect(2461, 1, 5);

	@Override
	public void startSuccess(Player player, WorldObject object) {
		if (player.getArea() instanceof final ScabarasEncounter encounter) {
			final boolean north = player.getY() < object.getY();
			final Location dest = encounter.getLocation(north ? NORTH_LOCATION : SOUTH_LOCATION);
			WorldTasksManager.schedule(new WorldTask() {

				private int ticks;

				@Override public void run() {
					if (ticks == 0) {
						player.setAnimation(Animation.JUMP);
						player.setForceMovement(new ForceMovement(player.getLocation(), 15, object, 35, north ? ForceMovement.NORTH : ForceMovement.SOUTH));
						player.sendSound(JUMP_FIRST_SOUND);
					} else if (ticks == 1)
						player.setLocation(object);
					else if (ticks == 2) {
						player.setAnimation(Animation.JUMP);
						player.sendSound(JUMP_SECOND_SOUND);
						player.setForceMovement(new ForceMovement(player.getLocation(), 15, dest, 35, north ? ForceMovement.NORTH : ForceMovement.SOUTH));
					} else if (ticks == 3) {
						player.setLocation(dest);
						stop();
					}
					ticks++;
				}
			}, 0, 0);
		}
	}

	@Override public int getLevel(WorldObject object) {
		return 0;
	}

	@Override public int[] getObjectIds() {
		return new int[] {45396};
	}

	@Override public int getDuration(boolean success, WorldObject object) {
		return 3;
	}

	@Override public double getSuccessXp(WorldObject object) {
		return 5;
	}

	@Override public Location getRouteEvent(Player player, WorldObject object) {
		if (player.getArea() instanceof final ScabarasEncounter encounter) {
			final int northY = encounter.getY(NORTH_LOCATION.getY());
			return encounter.getLocation(player.getY() < northY ? SOUTH_LOCATION : NORTH_LOCATION);
		}
		return player.getLocation();
	}
}
