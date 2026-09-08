package org.jesse.plugins.object;

import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.world.object.WorldObject;
import org.jesse.plugins.dialogue.skills.WeavingD;

/**
 * @author Kris | 10. nov 2017 : 22:31.51
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 */
public final class LoomObject implements ObjectAction {

	@Override
	public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
		player.getDialogueManager().start(new WeavingD(player));
	}

	@Override
	public Object[] getObjects() {
		return new Object[] { "Loom" };
	}

}
