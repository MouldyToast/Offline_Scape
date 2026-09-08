package org.jesse.game.content.tombsofamascut.lobby;

import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.world.object.ObjectHandler;
import org.jesse.game.world.object.WorldObject;
import org.jesse.plugins.dialogue.OptionDialogue;

/**
 * @author Savions.
 */
public class TOARaidEntryAction implements ObjectAction {

	private static final Location OBELISK_LOCATION = new Location(3358, 9119, 0);

	@Override public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
		TOALobbyParty currentLobbyParty = TOALobbyParty.getCurrentParty(player);

		if (currentLobbyParty == null) {
			final OptionDialogue dialogue = new OptionDialogue(player, "You are currently not in a raiding party.", new String[] {"Form or join a party.", "Cancel."},
					new Runnable[] {() -> ObjectHandler.handle(player, 46068, OBELISK_LOCATION, false, 1), null});
			player.getDialogueManager().start(dialogue);
		} else {
			player.getTOAManager().enterRaid();
		}
	}

	@Override public Object[] getObjects() {
		return new Object[] {46089};
	}
}
