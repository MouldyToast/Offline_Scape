package org.jesse.plugins.dialogue.skills;

import org.jesse.game.content.skills.crafting.actions.LeatherCrafting;
import org.jesse.game.item.Item;
import org.jesse.game.world.entity.player.Player;
import org.jesse.plugins.dialogue.SkillDialogue;

/**
 * @author Tommeh | 26 aug. 2018 | 15:34:23
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 *      profile</a>}
 */
public class LeatherCraftingD extends SkillDialogue {
	public LeatherCraftingD(final Player player, final Item... items) {
		super(player, items);
	}

	@Override
	public void run(final int slotId, final int amount) {
		final int category = player.getNumericTemporaryAttribute("LeatherCategory").intValue();
		if (category != -1) {
			player.getActionManager().setAction(new LeatherCrafting(category, slotId, amount));
		}
	}
}
