package org.jesse.game.content.tombsofamascut.object;

import org.jesse.game.content.tombsofamascut.encounter.ScabarasEncounter;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Savions.
 */
public class LightTabletAction implements ObjectAction {

	@Override public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
		if ("Flip".equalsIgnoreCase(option) && player.getArea() instanceof final ScabarasEncounter scabarasEncounter) {
			scabarasEncounter.flipLightTablet(player, object.getLocation());
		}
	}

	@Override public Object[] getObjects() {
		return new Object[] {ScabarasEncounter.LIGHT_PLATE_ID};
	}
}
