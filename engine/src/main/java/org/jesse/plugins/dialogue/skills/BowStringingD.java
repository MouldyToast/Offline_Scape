package org.jesse.plugins.dialogue.skills;

import org.jesse.game.content.skills.fletching.FletchingDefinitions.BowFletchingData;
import org.jesse.game.content.skills.fletching.actions.BowFletching;
import org.jesse.game.world.entity.player.Player;
import org.jesse.plugins.dialogue.SkillDialogue;

/**
 * @author Tommeh | 25 aug. 2018 | 19:12:02
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class BowStringingD extends SkillDialogue {

	private final BowFletchingData data;

	public BowStringingD(final Player player, final BowFletchingData data) {
		super(player, data.getProduct());
		this.data = data;
	}

	@Override
	public void run(final int slotId, final int amount) {
		if (data != null) {
			player.getActionManager().setAction(new BowFletching(data, amount));
		}
	}

}
