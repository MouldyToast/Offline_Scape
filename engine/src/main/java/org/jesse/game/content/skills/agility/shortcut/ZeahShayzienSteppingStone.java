package org.jesse.game.content.skills.agility.shortcut;

import org.jesse.game.content.skills.agility.Shortcut;
import org.jesse.game.task.WorldTask;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.masks.ForceMovement;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Kris | 10/05/2019 17:41
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ZeahShayzienSteppingStone implements Shortcut {

    private static final Location WEST = new Location(1603, 3571, 0);

    private static final Location EAST = new Location(1607, 3571, 0);

    private static final Location ALT_WEST = new Location(1610, 3570, 0);

    private static final Location ALT_EAST = new Location(1614, 3570, 0);

    @Override
    public void startSuccess(Player player, WorldObject object) {
        final boolean west = player.getX() < object.getX();
        final Location dest = object.getId() == 29729 ? (west ? ALT_EAST : ALT_WEST) : (west ? EAST : WEST);
        WorldTasksManager.schedule(new WorldTask() {

            private int ticks;

            @Override
            public void run() {
                if (ticks == 0) {
                    player.setAnimation(Animation.JUMP);
                    player.setForceMovement(new ForceMovement(player.getLocation(), 15, object, 35, west ? ForceMovement.EAST : ForceMovement.WEST));
                } else if (ticks == 1)
                    player.setLocation(object);
                else if (ticks == 2) {
                    player.setAnimation(Animation.JUMP);
                    player.setForceMovement(new ForceMovement(player.getLocation(), 15, dest, 35, west ? ForceMovement.EAST : ForceMovement.WEST));
                } else if (ticks == 3) {
                    player.setLocation(dest);
                    stop();
                }
                ticks++;
            }
        }, 0, 0);
    }

    @Override
    public int getLevel(WorldObject object) {
        return 40;
    }

    @Override
    public int[] getObjectIds() {
        return new int[] { 29730, 29729 };
    }

    @Override
    public int getDuration(boolean success, WorldObject object) {
        return 3;
    }

    @Override
    public double getSuccessXp(WorldObject object) {
        return 0;
    }

    @Override
    public Location getRouteEvent(Player player, WorldObject object) {
        if (object.getId() == ObjectId.STEPPING_STONE_29729) {
            return player.getX() < object.getX() ? ALT_WEST : ALT_EAST;
        }
        return player.getX() < object.getX() ? WEST : EAST;
    }
}
