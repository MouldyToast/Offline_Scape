package org.jesse.plugins.dialogue.skills;

import org.jesse.game.content.skills.smithing.DragonArmourySmithing;
import org.jesse.game.world.entity.player.Player;
import org.jesse.plugins.dialogue.SkillDialogue;

/**
 * @author Tommeh | 22 jul. 2018 | 17:25:59
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class AncientForgeD extends SkillDialogue {
	public AncientForgeD(Player player) {
		super(player, "How many do you wish to make?", DragonArmourySmithing.DRAGON_KITESHIELD, DragonArmourySmithing.DRAGON_PLATEBODY);
	}

	@Override
	public void run(final int slotId, final int amount) {
		final String item = slotId == 0 ? "Kiteshield" : "Platebody";
		player.getActionManager().setAction(new DragonArmourySmithing(item, amount));
	}
}
