package org.jesse.game.world.entity.npc.impl;

import org.jesse.game.content.achievementdiary.diaries.KourendDiary;
import org.jesse.game.util.Direction;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.npc.Spawnable;
import org.jesse.game.world.entity.player.Player;

/**
 * @author Tommeh | 16/11/2019 | 21:00
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class Lizardman extends NPC implements Spawnable {
    public Lizardman(int id, Location tile, Direction facing, int radius) {
        super(id, tile, facing, radius);
    }

    @Override
    public void onDeath(final Entity source) {
        super.onDeath(source);
        if (source instanceof Player player) {
            player.getAchievementDiaries().update(KourendDiary.KILL_A_LIZARDMAN);
        }
    }

    @Override
    public boolean validate(int id, String name) {
        return name.equals("lizardman");
    }
}
