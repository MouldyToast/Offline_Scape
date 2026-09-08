package org.jesse.game.model.item.pluginextensions;

import org.jesse.game.item.Item;
import org.jesse.game.model.item.degradableitems.ChargesManager;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.container.ContainerWrapper;

import java.text.DecimalFormat;

/**
 * @author Kris | 25. aug 2018 : 17:05:15
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public interface ChargeExtension {
	DecimalFormat FORMATTER = ChargesManager.FORMATTER;

	void removeCharges(final Player player, final Item item, final ContainerWrapper wrapper, int slotId, final int amount);

	default void checkCharges(final Player player, final Item item) {
		player.getChargesManager().notifyPlayerOfChargesLeft(item);
	}
}
