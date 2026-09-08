package org.jesse.game.content.skills.agility.shortcut;

import org.jesse.game.content.skills.agility.Shortcut;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.masks.ForceMovement;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Kris | 10/05/2019 22:19
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class EaglesPeakShortcut implements Shortcut {
    @Override
    public int getLevel(final WorldObject object) {
        return 25;
    }

    @Override
    public int[] getObjectIds() {
        return new int[] {
                19849
        };
    }
    private static final Animation CLIMB = new Animation(740);

    @Override
    public int getDuration(final boolean success, final WorldObject object) {
        return 6;
    }

    @Override
    public Location getRouteEvent(final Player player, final WorldObject object) {
        return object.withinDistance(2324, 3497, 1) ? new Location(2324, 3497, 0) : new Location(2322, 3502, 0);
    }

    @Override
    public void startSuccess(final Player player, final WorldObject object) {
        Location destination = !object.withinDistance(2324, 3497, 1) ? new Location(2324, 3497, 0)
                                                                    : new Location(2322, 3502, 0);
        ForceMovement forceMovement = new ForceMovement(destination, 180, ForceMovement.SOUTH);
        player.setAnimation(CLIMB);
        player.setForceMovement(forceMovement);
        WorldTasksManager.schedule(() -> {
            player.setAnimation(Animation.STOP);
            player.setLocation(destination);
        }, 5);
    }

    @Override
    public double getSuccessXp(final WorldObject object) {
        return 0;
    }
}
