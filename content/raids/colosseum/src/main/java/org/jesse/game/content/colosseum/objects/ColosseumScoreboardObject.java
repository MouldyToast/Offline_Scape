package org.jesse.game.content.colosseum.objects;

import org.jesse.game.GameInterface;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;

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
