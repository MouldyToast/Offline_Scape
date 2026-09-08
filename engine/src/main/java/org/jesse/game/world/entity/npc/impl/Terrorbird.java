package org.jesse.game.world.entity.npc.impl;

import org.jesse.game.content.achievementdiary.diaries.WesternProvincesDiary;
import org.jesse.game.util.Direction;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.npc.Spawnable;
import org.jesse.game.world.entity.player.Player;

/**
 * @author Tommeh | 17-11-2018 | 16:57
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class Terrorbird extends NPC implements Spawnable {
    public Terrorbird(int id, Location tile, Direction facing, int radius) {
        super(id, tile, facing, radius);
    }

    @Override
    public void onDeath(final Entity source) {
        super.onDeath(source);
        if (source instanceof Player) {
            final Player player = (Player) source;
            player.getAchievementDiaries().update(WesternProvincesDiary.KILL_TERRORBIRD);
        }
    }

    @Override
    public boolean validate(int id, String name) {
        return name.equals("terrorbird");
    }
}
