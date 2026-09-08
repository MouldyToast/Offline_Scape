package org.jesse.game.content.skills.thieving;

import org.jesse.game.world.entity.player.PlayerAttributesKt;
import org.jesse.game.content.achievementdiary.DiaryReward;
import org.jesse.game.content.achievementdiary.DiaryUtil;
import org.jesse.game.model.item.SkillcapePerk;
import org.jesse.game.util.Utils;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.SkillConstants;
import org.jesse.game.world.entity.player.privilege.MemberRank;

/**
 * @author Kris | 21. okt 2017 : 14:46.17
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 */
public final class Thieving {
	public static boolean success(final Player player, final int requirement) {
		final int level = player.getSkills().getLevel(SkillConstants.THIEVING);
		final double baseChance = 5.0 / 833 * level;
		final double reqChance = 0.49 - (requirement * 0.0032) - 0.02;
		double chance = baseChance + reqChance;
		if (SkillcapePerk.THIEVING.isEffective(player)) {
			chance *= 1.1F;
		}
		if (player.getMemberRank().equalToOrGreaterThan(MemberRank.RUBY)) {
			chance *= 1.05F;
		}
		if (DiaryUtil.eligibleFor(DiaryReward.ARDOUGNE_CLOAK3, player)) {
			chance *= 1.1F;
		} else if (player.inArea("Ardougne") && DiaryUtil.eligibleFor(DiaryReward.ARDOUGNE_CLOAK2, player)) {
			chance *= 1.1F;
		}
		if(PlayerAttributesKt.getFlaggedAsBot(player))
			chance *= 0.01;
		return Utils.randomDouble() < chance;
	}
}
