package org.jesse.game.content.colosseum.objects;

import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;
import org.jesse.plugins.object.LadderOA;

@SuppressWarnings("unused")
public class FortisColosseumExit implements ObjectAction {

	private static final Location DEST = new Location(1795, 3106);

	@Override
	public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
		player.lock(1);
		player.setAnimation(LadderOA.CLIMB_UP);
		WorldTasksManager.schedule(() -> player.setLocation(DEST));
	}

	@Override
	public Object[] getObjects() {
		return new Object[]{ObjectId.STAIRS_50750};
	}

}
