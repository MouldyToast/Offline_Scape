package org.jesse.plugins.item;

import org.jesse.game.content.skills.magic.spells.teleports.TeleportCollection;
import org.jesse.game.model.item.pluginextensions.ItemPlugin;

/**
 * @author Kris | 25. aug 2018 : 22:42:26
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class RoyalSeedPod extends ItemPlugin {

	@Override
	public void handle() {
		bind("Commune", (player, item, slotId) -> {
			TeleportCollection.ROYAL_SEED_POD_TELEPORT_STRONGHOLD.teleport(player);
		});
	}

	@Override
	public int[] getItems() {
		return new int[] { 19564 };
	}

}
