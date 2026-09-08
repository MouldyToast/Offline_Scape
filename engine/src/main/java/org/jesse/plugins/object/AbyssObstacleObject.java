package org.jesse.plugins.object;

import org.jesse.game.content.skills.runecrafting.abyss.AbyssObstacle;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.world.object.WorldObject;

import java.util.ArrayList;

/**
 * @author Tommeh | 29 jul. 2018 | 22:14:34
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class AbyssObstacleObject implements ObjectAction {
	public static final Animation GAP_START_ANIMATION = new Animation(1331);
	public static final Animation GAP_END_ANIMATION = new Animation(1332);
	public static final Animation EYES_ANIMATION = new Animation(1057);

	@Override
	public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
		final Number delay = player.getNumericTemporaryAttribute("abyss obstacle delay");
		if (delay.longValue() > System.currentTimeMillis()) {
			return;
		}
		final AbyssObstacle obstacle = AbyssObstacle.get(object.getId());
		final Location destination = obstacle.getDestination();
		player.lock();
		WorldTasksManager.schedule(() -> obstacle.getType().getScript().handle(player, object, destination));
	}

	@Override
	public Object[] getObjects() {
		final ArrayList<Integer> list = new ArrayList<Integer>();
		for (final AbyssObstacle obstacle : AbyssObstacle.VALUES) {
			list.add(obstacle.getId());
		}
		return list.toArray(new Object[list.size()]);
	}
}
