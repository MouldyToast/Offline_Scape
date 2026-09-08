package org.jesse.plugins.item;

import org.jesse.game.item.Item;
import org.jesse.game.model.item.pluginextensions.ChargeExtension;
import org.jesse.game.model.item.pluginextensions.ItemPlugin;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.container.ContainerWrapper;
import org.jesse.utils.IntArray;

/**
 * @author Kris | 6. sept 2018 : 18:26:25
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class DragonfireShield extends ItemPlugin implements ChargeExtension {

	@Override
	public void handle() {
		bind("Activate", (player, item, slotId) ->
				player.getTemporaryAttributes().put("dragonfireBurst", true));
		bind("Operate", (player, item, slotId) ->
				player.getTemporaryAttributes().put("dragonfireBurst", true));
	    bind("Inspect", (player, item, slotId) ->
				player.getChargesManager().checkCharges(item));
	    bind("Empty", (player, item, container, slotId) ->
				player.getChargesManager().removeCharges(item, 50, container, slotId));
	}

	@Override
	public int[] getItems() {
		return DRAGONFIRE_SHIELDS;
	}

	public static final int[] DRAGONFIRE_SHIELDS =
			IntArray.of(11283, 11284, 21633, 21634, 22002, 22003);

	@Override
	public void removeCharges(final Player player, final Item item, final ContainerWrapper wrapper, int slotId, final int amount) {
		item.setCharges(Math.max(0, item.getCharges() - amount));
		if (!item.hasCharges()) {
			item.setId(item.getId() + 1);
		}
		wrapper.refresh(slotId);
	}

}
