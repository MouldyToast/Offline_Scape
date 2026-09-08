package org.jesse.game.content.tombsofamascut.lobby;

import org.jesse.game.item.ids.ItemId;
import org.jesse.game.util.Utils;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.world.object.WorldObject;
import org.jesse.plugins.dialogue.PlainChat;

/**
 * @author Savions.
 */
public class SackAction implements ObjectAction {

	@Override public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
		if (Utils.random(20) != 0) {
			player.getDialogueManager().start(new PlainChat(player, "You go to search the sack, but the bank camel glares at you menacingly and spits in your direction"));
		} else {
			player.getDialogueManager().start(new PlainChat(player, "You successfully take some grain while the bank camel isn't looking."));
			player.getInventory().addOrDrop(ItemId.GRAIN);
		}
	}

	@Override public Object[] getObjects() {
		return new Object[] { 46077 };
	}
}
