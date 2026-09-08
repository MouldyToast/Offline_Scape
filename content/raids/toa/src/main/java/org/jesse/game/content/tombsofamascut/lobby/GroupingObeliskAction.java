package org.jesse.game.content.tombsofamascut.lobby;

import org.jesse.game.GameInterface;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Savions.
 */
public class GroupingObeliskAction implements ObjectAction {

	@Override public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
		if (!"inspect".equalsIgnoreCase(option)) {
			return;
		}
		GameInterface.TOA_PARTY_OVERVIEW.open(player);
	}

	@Override public Object[] getObjects() {
		return new Object[] {46068};
	}
}
