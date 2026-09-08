package org.jesse.game.world.entity.player.calog;

import org.jesse.game.GameInterface;
import org.jesse.game.model.ui.Interface;
import org.jesse.game.util.AccessMask;
import org.jesse.game.world.entity.player.Player;

/**
 * @author Savions.
 */
public class CALogRewardInterface extends Interface {

	@Override protected void attach() {
		put(16, "Navigation");
	}

	@Override public void open(Player player) {
		super.open(player);
		player.getPacketDispatcher().sendComponentSettings(GameInterface.CA_REWARDS, 16, 10, 16, AccessMask.CLICK_OP1);
	}

	@Override protected void build() {
		bind("Navigation", (player, slotId, itemId, option) -> {
			System.out.println("REWARDS " + slotId);
			if (slotId == 10) {
				GameInterface.CA_OVERVIEW.open(player);
			} else if (slotId == 12) {
				GameInterface.CA_TASKS.open(player);
			} else if (slotId == 14) {
				GameInterface.CA_BOSS_OVERVIEW.open(player);
			}
		});
	}

	@Override public GameInterface getInterface() {
		return GameInterface.CA_REWARDS;
	}
}
