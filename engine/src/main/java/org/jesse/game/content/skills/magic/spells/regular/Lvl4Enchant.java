package org.jesse.game.content.skills.magic.spells.regular;

import org.jesse.game.content.skills.magic.Spellbook;
import org.jesse.game.content.skills.magic.actions.JewelleryEnchantment;
import org.jesse.game.content.skills.magic.actions.JewelleryEnchantment.JewelleryEnchantmentItem;
import org.jesse.game.content.skills.magic.actions.JewelleryEnchantmentType;
import org.jesse.game.content.skills.magic.spells.ItemSpell;
import org.jesse.game.item.Item;
import org.jesse.game.world.entity.player.Player;

/**
 * @author Tommeh | 21 mei 2018 | 17:20:23
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class Lvl4Enchant implements ItemSpell {
	@Override
	public int getDelay() {
		return 2000;
	}

	@Override
	public boolean spellEffect(final Player player, final Item item, final int slot) {
		final JewelleryEnchantment.JewelleryEnchantmentItem data = JewelleryEnchantmentItem.getDataByMaterial(item, JewelleryEnchantmentType.LVL4_ENCHANTMENT);
		if (data != null && JewelleryEnchantment.check(player, data)) {
			player.getActionManager().setAction(new JewelleryEnchantment(this, data, slot));
			return true;
		} else {
			player.sendMessage("This spell can only be cast on diamond amulets, rings, neckalces and bracelets and on shapes in the Mage Training Arena.");
			return false;
		}
	}

	@Override
	public Spellbook getSpellbook() {
		return Spellbook.NORMAL;
	}
}
