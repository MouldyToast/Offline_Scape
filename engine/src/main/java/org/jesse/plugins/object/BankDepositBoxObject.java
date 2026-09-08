package org.jesse.plugins.object;

import org.jesse.game.GameInterface;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Kris | 10. nov 2017 : 22:19.57
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 */
public final class BankDepositBoxObject implements ObjectAction {

	@Override
	public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        GameInterface.BANK_DEPOSIT_INTERFACE.open(player);
	}

	@Override
	public Object[] getObjects() {
		return new Object[] { "Bank deposit box", "Bank Deposit Chest" };
	}

}
