package org.jesse.game.world.entity.npc.impl.slayer;

import org.jesse.game.content.achievementdiary.diaries.KandarinDiary;
import org.jesse.game.util.Direction;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.npc.Spawnable;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.calog.CAType;

/**
 * @author Tommeh | 5-11-2018 | 18:20
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class FireGiant extends NPC implements Spawnable {
    public FireGiant(int id, Location tile, Direction facing, int radius) {
        super(id, tile, facing, radius);
    }

    @Override
    public void onDeath(final Entity source) {
        super.onDeath(source);
        if (source instanceof Player) {
            final Player player = (Player) source;
            player.getAchievementDiaries().update(KandarinDiary.KILL_A_FIRE_GIANT);
            player.getCombatAchievements().complete(CAType.THE_WALKING_VOLCANO);
        }
    }

    @Override
    public boolean validate(int id, String name) {
        return name.equals("fire giant");
    }
}
