package org.jesse.plugins.dialogue.skills;

import org.jesse.game.content.skills.fletching.FletchingDefinitions;
import org.jesse.game.content.skills.fletching.actions.LogsFletching;
import org.jesse.game.item.Item;
import org.jesse.game.world.entity.player.Player;
import org.jesse.plugins.dialogue.SkillDialogue;

/**
 * @author Tommeh | 25 aug. 2018 | 19:17:27
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class LogsFletchingD extends SkillDialogue {
	public LogsFletchingD(Player player, Item from, Item to) {
		super(player, FletchingDefinitions.getCategory(player, from, to));
	}

	@Override
	public void run(final int slotId, final int amount) {
		final int category = player.getNumericTemporaryAttribute("LogsCategory").intValue();
		if (category != -1) {
			player.getActionManager().setAction(new LogsFletching(category, slotId, amount));
		}
	}
}
