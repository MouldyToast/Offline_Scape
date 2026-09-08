package org.jesse.plugins.dialogue.skills;

import org.jesse.game.content.skills.crafting.actions.MoltenGlassCrafting;
import org.jesse.game.world.entity.player.Player;
import org.jesse.plugins.dialogue.SkillDialogue;

/**
 * @author Tommeh | 23 dec. 2017 : 19:02:55
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class MoltenGlassD extends SkillDialogue {

	public MoltenGlassD(Player player) {
		super(player, MoltenGlassCrafting.MOLTEN_GLASS);
	}

	@Override
	public void run(final int slotId, final int amount) {
		player.getActionManager().setAction(new MoltenGlassCrafting(amount));
	}	

}
