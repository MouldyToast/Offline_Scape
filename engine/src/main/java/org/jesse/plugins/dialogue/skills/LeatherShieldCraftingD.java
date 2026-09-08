package org.jesse.plugins.dialogue.skills;

import org.jesse.game.content.skills.crafting.CraftingDefinitions.LeatherShieldData;
import org.jesse.game.content.skills.crafting.actions.LeatherShieldCrafting;
import org.jesse.game.world.entity.player.Player;
import org.jesse.plugins.dialogue.SkillDialogue;

/**
 * @author Tommeh | 14 apr. 2018 | 18:12:58
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class LeatherShieldCraftingD extends SkillDialogue {
	
	private final LeatherShieldData data;

	public LeatherShieldCraftingD(final Player player, final LeatherShieldData data) {
		super(player, "How many would you like to make?", data.getProduct());
		this.data = data;
	}

	@Override
	public void run(final int slotId, final int amount) {
		if (data != null) {
			player.getActionManager().setAction(new LeatherShieldCrafting(data, amount));
		}
	}

}
