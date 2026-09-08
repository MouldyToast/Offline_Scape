package org.jesse.game.content.skills.agility.shortcut;

import org.jesse.game.content.skills.agility.Shortcut;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.masks.ForceMovement;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Kris | 09/10/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class LumbridgeBasementShortcut implements Shortcut {
    private static final Animation CRAWL = new Animation(749);

    @Override
    public int getLevel(WorldObject object) {
        return 1;
    }

    @Override
    public int[] getObjectIds() {
        return new int[] {6912};
    }

    @Override
    public int getDuration(boolean success, WorldObject object) {
        return 3;
    }

    @Override
    public void startSuccess(Player player, WorldObject object) {
        player.setAnimation(CRAWL);
        final Location dest = new Location(object.getX(), player.getY() <= object.getY() ? (object.getY() + 3) : (object.getY() - 1), player.getPlane());
        player.setForceMovement(new ForceMovement(player.getLocation(), 0, dest, 90, player.getY() <= object.getY() ? ForceMovement.NORTH : ForceMovement.SOUTH));
        WorldTasksManager.schedule(() -> player.setLocation(dest), 2);
    }

    @Override
    public double getSuccessXp(WorldObject object) {
        return 0;
    }
}
