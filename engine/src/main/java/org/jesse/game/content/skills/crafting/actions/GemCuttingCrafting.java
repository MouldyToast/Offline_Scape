package org.jesse.game.content.skills.crafting.actions;

import org.jesse.game.content.skills.crafting.CraftingDefinitions;
import org.jesse.game.content.skills.crafting.CraftingDefinitions.GemCuttingData;
import org.jesse.game.item.Item;
import org.jesse.game.util.Utils;
import org.jesse.game.world.entity.player.Action;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.SkillConstants;

/**
 * @author Tommeh | 25 aug. 2018 | 20:33:01
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 *      profile</a>}
 */
public class GemCuttingCrafting extends Action {
	private final GemCuttingData data;
	private final int slot;
	private final int amount;
	private int cycle;

	public static final boolean successful(final Player player, final int requirement) {
		final double baseChance = 5.0 / 833 * (player.getSkills().getLevel(SkillConstants.CRAFTING));
		final double reqChance = 0.5 - (requirement * 0.0093);
		return Utils.randomDouble() < baseChance + reqChance;
	}

	@Override
	public boolean start() {
		final int level = data.equals(GemCuttingData.AMETHYST) ? data.getLevel() + (2 * slot) : data.getLevel();
		if ((player.getSkills().getLevel(SkillConstants.CRAFTING)) < level) {
			player.sendMessage("You need a Crafting level of at least " + level + " to craft that.");
			return false;
		}
		return player.getInventory().containsItem(data.getMaterial());
	}

	public GemCuttingCrafting(GemCuttingData data, int slot, int amount) {
		this.data = data;
		this.slot = slot;
		this.amount = amount;
	}

	@Override
	public boolean process() {
		if (!player.getInventory().containsItem(data.getMaterial())) {
			return false;
		}
		return cycle < amount;
	}

	@Override
	public int processWithDelay() {
		player.setAnimation(data.getAnimation());
		player.getInventory().deleteItemsIfContains(new Item[] {data.getMaterial()}, () -> {
			if ((data.equals(GemCuttingData.OPAL) || data.equals(GemCuttingData.JADE) || data.equals(GemCuttingData.RED_TOPAZ)) && !successful(player, data.getLevel())) {
				player.sendFilteredMessage("You mis-hit the chisel and smash the " + data.getProducts()[slot].getDefinitions().getName().toLowerCase() + " to pieces!");
				player.getSkills().addXp(SkillConstants.CRAFTING, 3.8);
				player.getInventory().addItem(CraftingDefinitions.CRUSHED_GEM);
			} else {
				player.sendFilteredMessage("You cut the " + (data.equals(GemCuttingData.AMETHYST) ? "amethyst" : data.getProducts()[slot].getDefinitions().getName().toLowerCase()) + ".");
				player.getSkills().addXp(SkillConstants.CRAFTING, data.getXp());
				player.getInventory().addItem(data.getProducts()[slot]);
			}
			cycle++;
		});
		return 1;
	}
}
