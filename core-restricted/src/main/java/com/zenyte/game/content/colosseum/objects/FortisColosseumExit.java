package com.zenyte.game.content.colosseum.objects;

import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;
import com.zenyte.plugins.object.LadderOA;

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
