package org.jesse.game.content.skills.magic.spells.regular;

import org.jesse.game.content.skills.magic.Spellbook;
import org.jesse.game.content.skills.magic.actions.BoltEnchantment;
import org.jesse.game.content.skills.magic.actions.BoltEnchantment.BoltEnchantmentData;
import org.jesse.game.content.skills.magic.spells.DefaultSpell;
import org.jesse.game.item.Item;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.SkillConstants;
import org.jesse.plugins.dialogue.skills.BoltEnchantmentD;

import java.util.List;

/**
 * @author Kris | 15. juuli 2018 : 22:24:58
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class EnchantCrossbowBolt implements DefaultSpell {
	@Override
	public int getDelay() {
		return 0;
	}

	@Override
	public Spellbook getSpellbook() {
		return Spellbook.NORMAL;
	}

	@Override
	public boolean spellEffect(final Player player, final int optionId, final String option) {
		if (player.getSkills().getLevel(SkillConstants.MAGIC) < 4) {
			player.sendMessage("You need a Magic level of at least 4 to cast this spell.");
			return false;
		}
		final List<BoltEnchantment.BoltEnchantmentData> list = BoltEnchantmentData.getData(player, true);
		if (list.isEmpty()) {
			player.sendMessage("You have no bolts to enchant.");
			return false;
		}
		final Item[] items = new Item[list.size()];
		for (int i = 0; i < items.length; i++) {
			items[i] = list.get(i).getProduct();
		}
		player.getDialogueManager().start(new BoltEnchantmentD(player, this, list, items));
		return false;
	}
}
