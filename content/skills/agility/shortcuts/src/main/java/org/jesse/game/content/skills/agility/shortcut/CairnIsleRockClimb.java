package org.jesse.game.content.skills.agility.shortcut;

import org.jesse.game.content.skills.agility.Shortcut;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.masks.ForceMovement;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Kris | 10/05/2019 19:58
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class CairnIsleRockClimb implements Shortcut {

    private static final Animation CLIMB = new Animation(740);

    @Override
    public int getLevel(final WorldObject object) {
        return 15;
    }

    @Override
    public int[] getObjectIds() {
        return new int[] {
                2231
        };
    }

    @Override
    public int getDuration(final boolean success, final WorldObject object) {
        return 4;
    }

    @Override
    public void startSuccess(final Player player, final WorldObject object) {
        Location destination = object.getX() <= 2792 ?
                               new Location(2795, player.getY(), player.getPlane()) :
                               new Location(2791, player.getY(), player.getPlane());
        ForceMovement forceMovement = new ForceMovement(destination, 120, ForceMovement.WEST);
        player.setAnimation(CLIMB);
        player.setForceMovement(forceMovement);
        WorldTasksManager.schedule(() -> {
            player.setAnimation(Animation.STOP);
            player.setLocation(destination);
        }, 3);
    }

    @Override
    public double getSuccessXp(final WorldObject object) {
        return 0;
    }
}
