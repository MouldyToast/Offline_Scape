package org.jesse.plugins.object;

import org.jesse.game.GameInterface;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;

public class ChallengesScoreboard implements ObjectAction {

	@Override
	public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
		if(option.equalsIgnoreCase("View")) {
			if(player.getGameMode().isGroupIronman())
				GameInterface.CHALLENGES.open(player);
		}
	}

	@Override
	public Object[] getObjects() {
		return new Object[]{ObjectId.SCOREBOARD_40448};
	}

}
