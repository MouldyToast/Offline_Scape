package org.jesse.plugins.object;

import org.jesse.game.content.skills.thieving.Chest;
import org.jesse.game.content.skills.thieving.actions.ChestThieving;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Corey
 * @since 23/11/19
 */
public final class ThievingChestObject implements ObjectAction {
	
	@Override
	public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
		ChestThieving.handleChest(player, object, option.equalsIgnoreCase("Search for traps"));
	}
	
	@Override
	public Object[] getObjects() {
		return Chest.data.keySet().toArray();
	}
	
}
