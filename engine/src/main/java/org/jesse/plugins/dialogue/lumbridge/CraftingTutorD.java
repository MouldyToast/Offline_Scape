package org.jesse.plugins.dialogue.lumbridge;

import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 30. apr 2018 : 22:42:17
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server
 *      profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status
 *      profile</a>}
 */
public final class CraftingTutorD extends Dialogue {

	public CraftingTutorD(final Player player, final NPC npc) {
		super(player, npc);
	}

	@Override
	public void buildDialogue() {
		npc("The wheel on the spinner goes round and round, round and round, round and round; all day long.");
	}

}
