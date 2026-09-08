package org.jesse.game.content.skills.crafting.actions;

import org.jesse.game.content.achievementdiary.diaries.LumbridgeDiary;
import org.jesse.game.content.skills.crafting.CraftingDefinitions;
import org.jesse.game.content.skills.crafting.CraftingDefinitions.AmuletStringingData;
import org.jesse.game.item.Item;
import org.jesse.game.world.entity.player.Action;
import org.jesse.game.world.entity.player.SkillConstants;

/**
 * @author Tommeh | 27 mei 2018 | 00:30:39
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class AmuletStringingCrafting extends Action {
	private final AmuletStringingData data;

	public AmuletStringingCrafting(AmuletStringingData data) {
		this.data = data;
	}

	@Override
	public boolean start() {
		if (!player.getInventory().containsItem(CraftingDefinitions.BALL_OF_WOOL)) {
			player.sendMessage("You need a ball of wool to string the amulet.");
			return false;
		}
        return player.getInventory().containsItem(data.getMaterials()[0]);
    }

	@Override
	public boolean process() {
		return player.getInventory().containsItems(data.getMaterials());
	}

	@Override
	public int processWithDelay() {
		if (data.equals(AmuletStringingData.DIAMOND_AMULET)) {
			player.getAchievementDiaries().update(LumbridgeDiary.CRAFT_AMULET_OF_POWER, 2);
		}
		for (final Item item : data.getMaterials()) {
			player.getInventory().deleteItem(item);
		}
		player.getInventory().addItem(data.getProduct());
		player.getSkills().addXp(SkillConstants.CRAFTING, 4);
		player.sendFilteredMessage("You string the amulet.");
		return -1;
	}
}
