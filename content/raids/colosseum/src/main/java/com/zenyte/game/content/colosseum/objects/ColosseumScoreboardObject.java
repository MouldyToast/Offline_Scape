package com.zenyte.game.content.colosseum.objects;

import com.zenyte.game.GameInterface;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.ObjectId;
import com.zenyte.game.world.object.WorldObject;

@SuppressWarnings("unused")
public class ColosseumScoreboardObject implements ObjectAction {

	@Override
	public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
		GameInterface.COLOSSEUM_SCOREBOARD.open(player);
	}

	@Override
	public Object[] getObjects() {
		return new Object[]{ObjectId.COLOSSEUM_SCOREBOARD};
	}

}
