package org.jesse.game.content.tombsofamascut.object;

import org.jesse.game.content.tombsofamascut.encounter.ApmekenEncounter;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Savions.
 */
public class RoofSupportAction implements ObjectAction {

	@Override public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
		if (player.getArea() instanceof ApmekenEncounter encounter) {
			encounter.repairRoof(player, object);
		}
	}

	@Override public Object[] getObjects() {
		return new Object[] {45494};
	}
}
