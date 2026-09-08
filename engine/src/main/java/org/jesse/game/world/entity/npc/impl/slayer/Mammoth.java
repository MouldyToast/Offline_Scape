package org.jesse.game.world.entity.npc.impl.slayer;

import org.jesse.game.content.achievementdiary.diaries.WildernessDiary;
import org.jesse.game.util.Direction;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.npc.Spawnable;
import org.jesse.game.world.entity.player.Player;

/**
 * @author Tommeh | 4 okt. 2018 | 18:40:10
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public final class Mammoth extends NPC implements Spawnable {
	public Mammoth(final int id, final Location tile, final Direction facing, final int radius) {
		super(id, tile, facing, radius);
	}

	@Override
	public void onDeath(final Entity source) {
		super.onDeath(source);
		if (source instanceof Player) {
			final Player player = (Player) source;
			player.getAchievementDiaries().update(WildernessDiary.KILL_A_MAMMOTH);
		}
	}

	@Override
	public boolean validate(final int id, final String name) {
		return name.equals("mammoth");
	}
}
