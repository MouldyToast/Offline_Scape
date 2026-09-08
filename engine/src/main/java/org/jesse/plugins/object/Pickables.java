package org.jesse.plugins.object;

import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.world.object.PickPlant;
import org.jesse.game.world.object.WorldObject;

public class Pickables implements ObjectAction {

	@Override
	public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
		if (option.equalsIgnoreCase("Pick"))
			player.getActionManager().setAction(new PickPlant(object));
	}

	@Override
	public Object[] getObjects() {
		return PickPlant.Pickables.map.keySet().toArray(new Object[0]);
	}

}
