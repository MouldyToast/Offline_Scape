package org.jesse.game.content.tombsofamascut.object;

import org.jesse.game.content.tombsofamascut.encounter.ScabarasEncounter;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Savions.
 */
public class AncientButtonAction implements ObjectAction {

	@Override public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
		if (player.getArea() instanceof final ScabarasEncounter encounter) {
			encounter.pressAncientButton(player);
		}
	}

	@Override public Object[] getObjects() {
		return new Object[] {ScabarasEncounter.PRESSURE_BUTTON_ID};
	}
}
