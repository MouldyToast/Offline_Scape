package org.jesse.plugins.dialogue.followers;

import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 2. nov 2017 : 23:57.13
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 */
public final class PetSmokeDevilD extends Dialogue {

	public PetSmokeDevilD(final Player player, final NPC npc) {
		super(player, npc);
	}

	@Override
	public void buildDialogue() {
		player("Your kind comes in three different sizes?");
		npc("Four, actually.");
		player("Wow. Whoever created you wasn't very creative. You're just resized versions of one another!");
	}

}
