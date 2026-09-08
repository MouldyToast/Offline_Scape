package org.jesse.game.content.skills.agility.shortcut;

import org.jesse.game.content.achievementdiary.diaries.KandarinDiary;
import org.jesse.game.content.skills.agility.Shortcut;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.masks.RenderAnimation;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Kris | 10/05/2019 17:14
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class CoalTrucksLogBalance implements Shortcut {

    private static final RenderAnimation RENDER = new RenderAnimation(763, 762, 762, 762, 762, 762, 762);

    @Override
    public RenderAnimation getRenderAnimation() {
        return RENDER;
    }


    @Override
    public int getLevel(final WorldObject object) {
        return 20;
    }

    @Override
    public Location getRouteEvent(final Player player, final WorldObject object) {
        return object.getX() == 2599 ? new Location(2598, 3477, 0) : new Location(2603, 3477, 0);
    }

    @Override
    public int[] getObjectIds() {
        return new int[] {
                23274
        };
    }

    @Override
    public int getDuration(final boolean success, final WorldObject object) {
        return 3;
    }

    @Override
    public void startSuccess(final Player player, final WorldObject object) {
        if (player.getX() == 2598 && player.getY() == 3477) {
            player.addWalkSteps(2603, 3477, -1, false);
        } else {
            player.addWalkSteps(2598, 3477, -1, false);
        }
        WorldTasksManager.schedule(() -> player.getAchievementDiaries().update(KandarinDiary.CROSS_COAL_TRUCK_LOG), 2);
    }

    @Override
    public double getSuccessXp(final WorldObject object) {
        return 0;
    }
}
