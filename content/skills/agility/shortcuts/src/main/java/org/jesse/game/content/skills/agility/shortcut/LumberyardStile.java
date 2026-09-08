package org.jesse.game.content.skills.agility.shortcut;

import org.jesse.game.content.skills.agility.Shortcut;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.util.DirectionUtil;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.masks.ForceMovement;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Kris | 16/06/2019 11:43
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class LumberyardStile implements Shortcut {
    private static final Animation CLIMB = new Animation(839);

    @Override
    public Location getRouteEvent(final Player player, final WorldObject object) {
        return player.getY() < object.getY() ? new Location(3308, 3491, 0) : new Location(3308, 3494, 0);
    }

    @Override
    public void startSuccess(final Player player, final WorldObject object) {
        player.faceObject(object);
        final Location destination = object.transform(0, player.getY() > object.getY() ? -1 : 2, 0);
        final ForceMovement forceMovement = new ForceMovement(destination, 60, DirectionUtil.getFaceDirection(destination.getX() - player.getX(), destination.getY() - player.getY()));
        player.setAnimation(CLIMB);
        player.setForceMovement(forceMovement);
        WorldTasksManager.schedule(() -> {
            player.setAnimation(Animation.STOP);
            player.setLocation(destination);
        }, 1);
    }

    @Override
    public String getEndMessage(final boolean success) {
        return success ? "You climb over the broken fence." : null;
    }

    @Override
    public int getLevel(final WorldObject object) {
        return 0;
    }

    @Override
    public int[] getObjectIds() {
        return new int[] {2618};
    }

    @Override
    public int getDuration(final boolean success, final WorldObject object) {
        return 2;
    }

    @Override
    public double getSuccessXp(final WorldObject object) {
        return 0;
    }
}
