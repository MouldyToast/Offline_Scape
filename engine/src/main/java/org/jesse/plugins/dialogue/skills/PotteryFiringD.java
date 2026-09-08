package org.jesse.plugins.dialogue.skills;

import org.jesse.game.content.skills.crafting.CraftingDefinitions;
import org.jesse.game.content.skills.crafting.CraftingDefinitions.PotteryFiringData;
import org.jesse.game.content.skills.crafting.actions.PotteryFiringCrafting;
import org.jesse.game.world.entity.player.Player;
import org.jesse.plugins.dialogue.SkillDialogue;

/**
 * @author Tommeh | 27 aug. 2018 | 18:47:35
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class PotteryFiringD extends SkillDialogue {
	public PotteryFiringD(Player player) {
		super(player, PotteryFiringData.POT.getProduct(), PotteryFiringData.PIE_DISH.getProduct(), PotteryFiringData.BOWL.getProduct(), PotteryFiringData.EMPTY_PLANT_POT.getProduct(), PotteryFiringData.POT_LID.getProduct());
	}

	@Override
	public void run(final int slotId, final int amount) {
		final CraftingDefinitions.PotteryFiringData data = PotteryFiringData.VALUES.get(slotId);
		if (data != null) {
			player.getActionManager().setAction(new PotteryFiringCrafting(data, amount));
		}
	}
}
