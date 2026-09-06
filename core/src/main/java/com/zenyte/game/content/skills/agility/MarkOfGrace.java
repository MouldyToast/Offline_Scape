package com.zenyte.game.content.skills.agility;

import com.zenyte.game.content.achievementdiary.AchievementDiariesKeys;
import com.zenyte.game.content.achievementdiary.diaries.ArdougneDiary;
import com.zenyte.game.content.achievementdiary.diaries.KandarinDiary;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.SkillConstants;
import com.zenyte.game.world.entity.player.container.impl.Inventory;
import com.zenyte.game.world.entity.player.privilege.MemberRank;

/**
 * @author Tommeh | 7 sep. 2018 | 19:15:48
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 *      profile</a>}
 */
public class MarkOfGrace {
	private static final int MARK_OF_GRACE = ItemId.MARK_OF_GRACE;

	public static void spawn(final Player player, final Location[] locations, final int rarity, final int threshold) {
		int random = 6;
		if (player.getSkills().getLevel(SkillConstants.AGILITY) > threshold + 20) {
			random *= 0.8;
		}
		int amt = 1;
		final int endRarity = getRarity(player, rarity);
		if (Utils.random(endRarity) < random) {
			var mog = new Item(MARK_OF_GRACE, amt);
			player.getCollectionLog().add(mog);
			if (player.isMember()) {
				final Inventory inventory = player.getInventory();
				if (inventory.getFreeSlots() > 0 || inventory.containsItem(ItemId.MARK_OF_GRACE)) {
					player.getInventory().addItem(mog);
					return;
				}
			}
			World.spawnFloorItem(mog, locations[Utils.random(locations.length - 1)], player, 10000, 0);
		}
	}

	private static Integer getRarity(Player player, int base) {
		int endRarity = base;
		if (player.getLocation().getRegionId() == 1111) {
			if (AchievementDiariesKeys.achievementDiaries(player).isAllCompleted(ArdougneDiary.ELITE))
				endRarity *= 0.75; //25%
		}
		if (player.getLocation().getRegionId() == 10806) {
			if (AchievementDiariesKeys.achievementDiaries(player).isAllCompleted(KandarinDiary.EASY))
				endRarity *= 0.95; //5%
		}
		if (player.getLocation().getRegionId() == 10806) {
			if (AchievementDiariesKeys.achievementDiaries(player).isAllCompleted(KandarinDiary.MEDIUM))
				endRarity *= 0.90; //10%
		}
		if (player.getLocation().getRegionId() == 10806) {
			if (AchievementDiariesKeys.achievementDiaries(player).isAllCompleted(KandarinDiary.HARD))
				endRarity *= 0.85; //15%
		}

		switch (player.getMemberRank()) {
			case TOPAZ ->  endRarity *= 0.95;
			case SAPPHIRE -> endRarity *= 0.92;
			case EMERALD -> endRarity *= 0.9;
			case RUBY -> endRarity *= 0.88;
			case DIAMOND -> endRarity *= 0.85;
			case DRAGONSTONE -> endRarity *= 0.82;
			case ONYX -> endRarity *= 0.8;
			case ZENYTE -> endRarity *= 0.75;
			case ENCHANTED -> endRarity *= 0.7;
			case GOLD -> endRarity *= 0.6;
			case ETERNAL -> endRarity *= 0.4;
			case NEBULA -> endRarity *= 0.25;
			case CATALYTIC -> endRarity *= 0;
		}
		return endRarity;
	}
}
