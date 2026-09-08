package org.jesse.game.content.tombsofamascut.lobby;

import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.util.Direction;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.cutscene.FadeScreen;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Savions.
 */
public class TOAEntranceAction implements ObjectAction {

	private static final Location OUTSIDE_LOCATION = new Location(3357, 2713);
	private static final Location INSIDE_LOCATION = new Location(3359, 9128);

	@Override public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
		final FadeScreen screen = new FadeScreen(player, () -> {
			player.setLocation(object.getId() == 46087 ? OUTSIDE_LOCATION : INSIDE_LOCATION);
			player.faceDirection(object.getId() == 46087 ? Direction.NORTH_WEST : Direction.SOUTH);
		});
		screen.fade();
		WorldTasksManager.schedule(screen::unfade, 2);
	}

	@Override public Object[] getObjects() {
		return new Object[] {46087, 44596};
	}
}
