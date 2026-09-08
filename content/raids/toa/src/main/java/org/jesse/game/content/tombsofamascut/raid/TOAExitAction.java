package org.jesse.game.content.tombsofamascut.raid;

import org.jesse.game.content.tombsofamascut.TOAManager;
import org.jesse.game.content.tombsofamascut.lobby.TOALobbyParty;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.world.object.WorldObject;
import org.jesse.plugins.dialogue.OptionDialogue;

/**
 * @author Savions.
 */
public class TOAExitAction implements ObjectAction {

	@Override public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
		TOARaidParty party = (TOARaidParty) player.getTOAManager().getRaidParty();
		if (party == null) {
			player.setLocation(TOAManager.OUTSIDE_LOCATION);
			return;
		}
		player.getTOAManager().startLeaveDialogue();
	}

	@Override public Object[] getObjects() {
		return new Object[] {45128, 45453, 45543, 45144, 46055, 45844, 45129};
	}
}