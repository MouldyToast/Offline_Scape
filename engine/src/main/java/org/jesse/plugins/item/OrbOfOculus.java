package org.jesse.plugins.item;

import org.jesse.game.model.item.pluginextensions.ItemPlugin;
import org.jesse.game.model.ui.PaneType;
import org.jesse.game.world.entity.Location;

/**
 * @author Kris | 25. aug 2018 : 22:35:07
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class OrbOfOculus extends ItemPlugin {

	@Override
	public void handle() {
		bind("Scry", (player, item, slotId) -> {
			player.lock(1);
			player.getPacketDispatcher().freecam(true);
			player.getPacketDispatcher().ifOpenTop(PaneType.ORB_OF_OCULUS.getId());
            player.getInterfaceHandler().getVisible().forcePut(PaneType.ORB_OF_OCULUS.getId() << 16, PaneType.ORB_OF_OCULUS.getId());
			player.getTemporaryAttributes().put("oculusStart", new Location(player.getLocation()));
		});
	}

	@Override
	public int[] getItems() {
		return new int[] { 22364 };
	}

}
