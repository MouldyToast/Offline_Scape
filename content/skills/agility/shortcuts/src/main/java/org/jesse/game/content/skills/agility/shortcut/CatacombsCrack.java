package org.jesse.game.content.skills.agility.shortcut;

import org.jesse.game.content.skills.agility.Shortcut;
import org.jesse.game.task.WorldTask;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.WorldObject;

import java.util.HashMap;
import java.util.Map;

public class CatacombsCrack implements Shortcut {
	
	private static final Map<Location, Location> SPOTS = new HashMap<>();
	
	private static final Animation ENTER = new Animation(746);
	private static final Animation EXIT = new Animation(748);
	
	private static final Location NECH_NORTH_EXIT = new Location(1706, 10078, 0);
	private static final Location NECH_SOUTH_EXIT = new Location(1716, 10056, 0);
	private static final Location ANKOU_NORTH_EXIT = new Location(1648, 10009, 0);
	private static final Location ANKOU_SOUTH_EXIT = new Location(1646, 10000, 0);

	private static final Location NECH_NORTH_EXIT_2 = new Location(1706, 10078 + (64 * 5), 0);
	private static final Location NECH_SOUTH_EXIT_2 = new Location(1716, 10056 + (64 * 5), 0);
	private static final Location ANKOU_NORTH_EXIT_2 = new Location(1648, 10009 + (64 * 5), 0);
	private static final Location ANKOU_SOUTH_EXIT_2 = new Location(1646, 10000 + (64 * 5), 0);


	private static final Location NECH_NORTH_EXIT_3 = new Location(1706, 10078 + (64 * 9), 0);
	private static final Location NECH_SOUTH_EXIT_3 = new Location(1716, 10056 + (64 * 9), 0);
	private static final Location ANKOU_NORTH_EXIT_3 = new Location(1648, 10009 + (64 * 9), 0);
	private static final Location ANKOU_SOUTH_EXIT_3 = new Location(1646, 10000 + (64 * 9), 0);


	private static final Location NECH_NORTH_EXIT_4 = new Location(1706, 10078 + (64 * 13), 0);
	private static final Location NECH_SOUTH_EXIT_4 = new Location(1716, 10056 + (64 * 13), 0);
	private static final Location ANKOU_NORTH_EXIT_4 = new Location(1648, 10009 + (64 * 13), 0);
	private static final Location ANKOU_SOUTH_EXIT_4 = new Location(1646, 10000 + (64 * 13), 0);
	
	private static final int NECH_CRACK_NORTH = 27961181;
	private static final int NECH_CRACK_SOUTH = 28125001;
	private static final int ANKOU_CRACK_NORTH = 27010840;
	private static final int ANKOU_CRACK_SOUTH = 26978065;
	
	static {
		SPOTS.put(NECH_NORTH_EXIT, NECH_SOUTH_EXIT);
		SPOTS.put(NECH_SOUTH_EXIT, NECH_NORTH_EXIT);
		SPOTS.put(ANKOU_NORTH_EXIT, ANKOU_SOUTH_EXIT);
		SPOTS.put(ANKOU_SOUTH_EXIT, ANKOU_NORTH_EXIT);

		SPOTS.put(NECH_NORTH_EXIT_2, NECH_SOUTH_EXIT_2);
		SPOTS.put(NECH_SOUTH_EXIT_2, NECH_NORTH_EXIT_2);
		SPOTS.put(ANKOU_NORTH_EXIT_2, ANKOU_SOUTH_EXIT_2);
		SPOTS.put(ANKOU_SOUTH_EXIT_2, ANKOU_NORTH_EXIT_2);

		SPOTS.put(NECH_NORTH_EXIT_3, NECH_SOUTH_EXIT);
		SPOTS.put(NECH_SOUTH_EXIT_3, NECH_NORTH_EXIT);
		SPOTS.put(ANKOU_NORTH_EXIT_3, ANKOU_SOUTH_EXIT);
		SPOTS.put(ANKOU_SOUTH_EXIT_3, ANKOU_NORTH_EXIT);

		SPOTS.put(NECH_NORTH_EXIT_4, NECH_SOUTH_EXIT_4);
		SPOTS.put(NECH_SOUTH_EXIT_4, NECH_NORTH_EXIT_4);
		SPOTS.put(ANKOU_NORTH_EXIT_4, ANKOU_SOUTH_EXIT_4);
		SPOTS.put(ANKOU_SOUTH_EXIT_4, ANKOU_NORTH_EXIT_4);
	}


	@Override
	public void startSuccess(Player player, WorldObject object) {
		final Location spot = SPOTS.get(player.getLocation());
		
		if(spot == null)
			return;
		
		WorldTasksManager.schedule(new WorldTask() {

			private int ticks;
			
			@Override
			public void run() {
				if(ticks == 0)
					player.setAnimation(ENTER);
				if(ticks == 1) {
					player.setAnimation(EXIT);
					player.setLocation(spot);
				} else if(ticks == 2)
					stop();
				
				ticks++;
			}
			
		}, 0, 0);
	}
	
	@Override
	public int getLevel(WorldObject object) {
		switch(object.getLocation().getX()) {
			case 1706:
			case 1716:
				return 34;
				
			case 1648:
			case 1646:
				return 17;
			
			default:
				return 1;
		}
	}

	@Override
	public int[] getObjectIds() {
		return new int[] { 28892 };
	}

	@Override
	public int getDuration(boolean success, WorldObject object) {
		return 3;
	}

	@Override
	public double getSuccessXp(WorldObject object) {
		return 0;
	}

}
