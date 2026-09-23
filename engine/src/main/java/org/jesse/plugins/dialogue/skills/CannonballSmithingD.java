package org.jesse.plugins.dialogue.skills;

import org.jesse.game.content.skills.smithing.CannonballSmithing;
import org.jesse.game.item.Item;
import org.jesse.game.item.ids.ItemId;
import org.jesse.game.world.entity.player.Player;
import org.jesse.plugins.dialogue.SkillDialogue;

/**
 * @author Tommeh | 10 jun. 2018 | 16:21:52
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class CannonballSmithingD extends SkillDialogue {

	public CannonballSmithingD(Player player) {
		super(player, "How many bars would you like to smith?", new Item(ItemId.STEEL_CANNONBALL));
	}

	@Override
	public void run(final int slotId, final int amount) {
		player.getActionManager().setAction(new CannonballSmithing(amount));
	}

}
