package org.jesse.game.content.tombsofamascut.raid;

import org.jesse.game.GameInterface;
import org.jesse.game.item.Item;
import org.jesse.game.model.ui.Interface;
import org.jesse.game.model.ui.SwitchPlugin;
import org.jesse.game.util.AccessMask;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.container.Container;

/**
 * @author Savions.
 */
public class TOASupplyBagInterface extends Interface implements SwitchPlugin {

	@Override public void open(Player player) {
		if (player.getTOAManager().getSuppliesContainer() != null) {
			player.getPacketDispatcher().sendUpdateItemContainer(player.getTOAManager().getSuppliesContainer());
		}
		super.open(player);
		player.getPacketDispatcher().sendComponentSettings(getInterface(), getComponent("Container"), 0, 27, AccessMask.CLICK_OP1, AccessMask.CLICK_OP2, AccessMask.CLICK_OP3, AccessMask.CLICK_OP4, AccessMask.CLICK_OP9, AccessMask.DRAG_DEPTH1, AccessMask.DRAG_TARGETABLE);
	}

	@Override protected void attach() {
		put(5, "Container");
	}

	@Override protected void build() {
		bind("Container", (player, slotId, itemId, option) -> player.getTOAManager().withdrawSpecificSupplies(slotId, option));
	}

	@Override public GameInterface getInterface() {
		return GameInterface.TOA_SUPPLIES_INV;
	}

	@Override public boolean switchItem(Player player, int fromComponent, int toComponent, int fromSlot, int toSlot) {
		final Container container = player.getTOAManager().getSuppliesContainer();
		if (container != null) {
			final Item from = container.get(fromSlot);
			final Item to = container.get(toSlot);
			if (from != null && to != null) {
				container.set(fromSlot, to);
				container.set(toSlot, from);
				container.refresh(player);
			}
		}
		return true;
	}
}
