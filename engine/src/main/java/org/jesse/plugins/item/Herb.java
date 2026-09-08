package org.jesse.plugins.item;

import org.jesse.game.content.skills.herblore.actions.Combine;
import org.jesse.game.content.skills.herblore.actions.Combine.HerbloreData;
import org.jesse.game.item.Item;
import org.jesse.game.model.item.pluginextensions.ItemPlugin;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.SkillConstants;
import org.jesse.game.world.entity.player.dailychallenge.challenge.SkillingChallenge;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import org.checkerframework.checker.index.qual.NonNegative;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 25. aug 2018 : 22:26:58
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public class Herb extends ItemPlugin {
	@Override
	public void handle() {
		bind("Clean", Herb::clean);
	}

	public static void clean(@NotNull final Player player, @NotNull final Item herbItem, @NonNegative final int slotId) {
		final Combine.HerbloreData herb = HerbloreData.get(herbItem.getId());
		if (player.getSkills().getLevel(SkillConstants.HERBLORE) < herb.getLevel()) {
			player.sendMessage("You need a Herblore level of " + herb.getLevel() + " to clean the " + herb.getMaterials()[0].getDefinitions().getName().replace("Grimy ", "") + ".");
			return;
		}
		if (herb.equals(HerbloreData.MARRENTILL)) {
			player.getDailyChallengeManager().update(SkillingChallenge.CLEAN_MARRENTILLS);
		} else if (herb.equals(HerbloreData.RANARR_WEED)) {
			player.getDailyChallengeManager().update(SkillingChallenge.CLEAN_RANARRS);
		} else if (herb.equals(HerbloreData.CADANTINE)) {
			player.getDailyChallengeManager().update(SkillingChallenge.CLEAN_CADANTINES);
		}
		player.getSkills().addXp(SkillConstants.HERBLORE, herb.getXp());
		player.getInventory().replaceItem(herb.getProduct().getId(), 1, slotId);
		player.sendFilteredMessage("You clean the " + herb + ".");
	}

	@Override
	public int[] getItems() {
		final IntArrayList list = new IntArrayList();
		for (final HerbloreData c : HerbloreData.HERBLORE.values()) {
			if (c.ordinal() >= HerbloreData.GUAM_LEAF.ordinal() && c.ordinal() <= HerbloreData.TORSTOL.ordinal()) {
				list.add(c.getMaterials()[0].getId());
			}
		}
		return list.toArray(new int[0]);
	}
}
