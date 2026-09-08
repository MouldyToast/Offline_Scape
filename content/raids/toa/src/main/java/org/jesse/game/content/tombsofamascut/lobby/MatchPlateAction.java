package org.jesse.game.content.tombsofamascut.lobby;

import org.jesse.game.content.tombsofamascut.encounter.ScabarasEncounter;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Savions.
 */
public class MatchPlateAction implements ObjectAction {

	@Override public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
		if (player.getArea() instanceof final ScabarasEncounter scabarasEncounter) {
			scabarasEncounter.handleMatchPlate(player, object);
		}
	}

	@Override public Object[] getObjects() {
		return new Object[] {45360};
	}

	@Override
	public int getStrategyDistance(WorldObject obj) {
		return 1;
	}
}
