package org.jesse.plugins.object;

import org.jesse.game.GameInterface;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;

@SuppressWarnings("unused")
public final class BankObject implements ObjectAction {

	@Override
	public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
		if (option.equals("Bank") || option.equals("Use")) {
            GameInterface.BANK.open(player);
		} else if (option.equalsIgnoreCase("Collect")) {
            GameInterface.GRAND_EXCHANGE_COLLECTION_BOX.open(player);
		}
	}

	@Override
	public Object[] getObjects() {
		return new Object[] { ObjectId.CHEST_42834, "Bank", "Bank booth", "Bank chest", "Open chest", "Bank counter", 46075, 46223};
	}
}
