package org.jesse.game.content.skills.magic.spells.lunar;

import org.jesse.game.content.skills.magic.Spellbook;
import org.jesse.game.content.skills.magic.spells.DefaultSpell;
import org.jesse.game.item.Item;
import org.jesse.game.util.Utils;
import org.jesse.game.world.entity.SoundEffect;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.masks.Graphics;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.SkillConstants;
import org.jesse.game.world.entity.player.container.impl.Inventory;

/**
 * @author Kris | 17. veebr 2018 : 2:31.16
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class SuperglassMake implements DefaultSpell {
	private static final Animation ANIM = new Animation(4413);
	private static final Graphics GFX = new Graphics(729, 0, 92);
	private static final SoundEffect sound = new SoundEffect(2896);

	@Override
	public int getDelay() {
		return 3000;
	}

	@Override
	public boolean spellEffect(final Player player, final int optionId, final String option) {
		if (!player.getInventory().containsItem(1783, 1)) {
			player.sendMessage("You need some sand to cast this spell.");
			return false;
		}
		if (!containsSecondaryIngredient(player)) {
			player.sendMessage("You need either some soda ash or seaweed to cast this spell.");
			return false;
		}
		player.setAnimation(ANIM);
		player.sendSound(sound);
		player.setGraphics(GFX);
		final Inventory inventory = player.getInventory();
		int sandCount = inventory.getAmountOf(1783);
		float xp = 0;
		for (int i = 0; i < 28; i++) {
			final Item item = inventory.getItem(i);
			if (item == null || item.getId() != 1781 && item.getId() != 401 && item.getId() != 21504 && item.getId() != 10978) {
				continue;
			}
			if (item.getId() == 21504) {
				inventory.deleteItem(i, item);
				int count = inventory.deleteItem(1783, 6).getSucceededAmount();
				inventory.addOrDrop(new Item(1775, count));
				for (int a = 0; a < count; a++) {
					final boolean extra = Utils.random(2) != 0;
					if (extra) {
						inventory.addOrDrop(new Item(1775, 1));
					}
				}
				xp += 60;
			} else {
				inventory.deleteItem(i, item);
				inventory.deleteItem(1783, 1);
				inventory.addOrDrop(new Item(1775, 1));
				final boolean extra = Utils.random(2) == 0;
				if (extra) {
					inventory.addOrDrop(new Item(1775, 1));
				}
				xp += 10;
			}
			if (--sandCount == 0) {
				break;
			}
		}
		this.addXp(player, 78);
		this.addXp(player, SkillConstants.CRAFTING, xp > 130 ? 130 : xp);
		return true;
	}

	private static final boolean containsSecondaryIngredient(final Player player) {
		final Inventory inventory = player.getInventory();
		for (int i = 0; i < 28; i++) {
			final Item item = inventory.getItem(i);
			if (item == null) {
				continue;
			}
			if (item.getId() == 1781 || item.getId() == 401 || item.getId() == 21504 || item.getId() == 10978) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Spellbook getSpellbook() {
		return Spellbook.LUNAR;
	}
}
