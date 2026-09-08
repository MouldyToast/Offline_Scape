package org.jesse.game.content.tombsofamascut.object;

import org.jesse.game.content.tombsofamascut.encounter.CrondisPuzzleEncounter;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Savions.
 */
public class WaterfallAction implements ObjectAction {

	@Override public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
		if (player.getArea() instanceof CrondisPuzzleEncounter crondisPuzzleEncounter) {
			crondisPuzzleEncounter.handleWaterfall(player, object);
		}
	}

	@Override public Object[] getObjects() {
		return new Object[] {CrondisPuzzleEncounter.WATERFALL_OBJECT_ID, CrondisPuzzleEncounter.WATERFALL_OBJECT_ID + 1};
	}
}
