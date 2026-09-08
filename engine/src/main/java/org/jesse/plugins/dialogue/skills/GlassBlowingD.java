package org.jesse.plugins.dialogue.skills;

import org.jesse.game.content.skills.crafting.CraftingDefinitions;
import org.jesse.game.content.skills.crafting.CraftingDefinitions.GlassBlowingData;
import org.jesse.game.content.skills.crafting.actions.GlassBlowingCrafting;
import org.jesse.game.world.entity.player.Player;
import org.jesse.plugins.dialogue.SkillDialogue;

/**
 * @author Tommeh | 15 mrt. 2018 : 19:15:15
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class GlassBlowingD extends SkillDialogue {
	public GlassBlowingD(final Player player) {
		super(player, GlassBlowingData.PRODUCTS);
	}

	@Override
	public void run(final int slotId, final int amount) {
		final CraftingDefinitions.GlassBlowingData data = GlassBlowingData.VALUES.get(slotId);
		if (data != null) {
			player.getActionManager().setAction(new GlassBlowingCrafting(data, amount));
		}
	}
}
