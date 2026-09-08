package org.jesse.plugins.dialogue.skills;

import org.jesse.game.content.skills.herblore.actions.Combine;
import org.jesse.game.content.skills.herblore.actions.Combine.HerbloreData;
import org.jesse.game.world.entity.player.Player;
import org.jesse.plugins.dialogue.SkillDialogue;

public class HerbloreD extends SkillDialogue {

	private final HerbloreData data;

	public HerbloreD(Player player, HerbloreData data) {
		super(player, data.getProduct());
		this.data = data;
	}

	@Override
	public void run(final int slotId, final int amount) {
		if (data != null) {
			player.getActionManager().setAction(new Combine(data, amount));
		}
	}

}
