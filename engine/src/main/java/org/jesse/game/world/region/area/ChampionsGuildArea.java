package org.jesse.game.world.region.area;

import org.jesse.game.content.achievementdiary.diaries.VarrockDiary;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.region.RSPolygon;

/**
 * @author Tommeh | 1 okt. 2018 | 18:33:16
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 *      profile</a>
 */
public class ChampionsGuildArea extends KingdomOfMisthalin {

	@Override
	public RSPolygon[] polygons() {
		return new RSPolygon[] { new RSPolygon(new int[][] { { 3188, 3363 }, { 3188, 3352 }, { 3199, 3352 }, { 3199, 3360 }, { 3195, 3360 }, { 3195, 3363 } }) };
	}

	@Override
	public void enter(Player player) {
	    super.enter(player);
		player.getAchievementDiaries().update(VarrockDiary.ENTER_CHAMPIONS_GUILD);
	}

	@Override
	public String name() {
		return "Champions' Guild";
	}

}
