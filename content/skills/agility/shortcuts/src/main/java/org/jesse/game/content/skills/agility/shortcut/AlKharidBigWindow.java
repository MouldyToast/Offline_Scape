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
 * @author Kris | 10/05/2019 16:57
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class AlKharidBigWindow implements Shortcut {
    private static final Animation ENTER = new Animation(746);
    private static final Animation EXIT = new Animation(748);

    @Override
    public int getLevel(final WorldObject object) {
        return 70;
    }

    @Override
    public int[] getObjectIds() {
        return new int[] {33348};
    }

    @Override
    public int getDuration(final boolean success, final WorldObject object) {
        return 0;
    }

    @Override
    public Location getRouteEvent(final Player player, final WorldObject object) {
        return player.getY() <= object.getY() ? object : new Location(object.getX(), object.getY() + 1, object.getPlane());
    }

    @Override
    public void startSuccess(final Player player, final WorldObject object) {
        player.faceObject(object);
        final Location destination = object.transform(0, player.getY() > object.getY() ? 0 : 1, 0);
        final ForceMovement forceMovement = new ForceMovement(destination, 60, DirectionUtil.getFaceDirection(destination.getX() - player.getX(), destination.getY() - player.getY()));
        player.setAnimation(ENTER);
        WorldTasksManager.schedule(() -> player.setAnimation(EXIT));
        player.setForceMovement(forceMovement);
        WorldTasksManager.schedule(() -> {
            player.setAnimation(Animation.STOP);
            player.setLocation(destination);
        }, 1);
    }

    @Override
    public double getSuccessXp(final WorldObject object) {
        return 0;
    }
}
