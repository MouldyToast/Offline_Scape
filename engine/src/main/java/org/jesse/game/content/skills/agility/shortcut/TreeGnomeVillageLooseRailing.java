package org.jesse.game.content.skills.agility.shortcut;

import org.jesse.game.content.skills.agility.Shortcut;
import org.jesse.game.task.WorldTask;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.masks.ForceMovement;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Kris | 10/05/2019 22:40
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class TreeGnomeVillageLooseRailing implements Shortcut {
    private static final Animation SQUEEZE = new Animation(1237);
    private static final Location EAST = new Location(2515, 3160, 0);

    @Override
    public void startSuccess(Player player, WorldObject object) {
        if (player.getLocation().matches(new Location(2516, 3161, 0))) {
            player.addWalkSteps(2515, 3161, 1, true);
            WorldTasksManager.schedule(() -> subHandle(player, object));
            return;
        }
        subHandle(player, object);
    }

    private void subHandle(final Player player, final WorldObject object) {
        player.faceObject(object);
        final boolean west = player.getLocation().getPositionHash() == object.getPositionHash();
        WorldTasksManager.schedule(new WorldTask() {

            private int ticks;

            @Override
            public void run() {
                if(ticks == 0) {
                    player.setAnimation(SQUEEZE);
                    player.setForceMovement(new ForceMovement(west ? EAST : object, 60, west ? ForceMovement.SOUTH : ForceMovement.NORTH));
                } else if(ticks == 2) {
                    player.setLocation(west ? EAST : object);
                    stop();
                }
                ticks++;
            }

        }, 0, 0);
    }

    @Override
    public int getLevel(WorldObject object) {
        return 0;
    }

    @Override
    public int[] getObjectIds() {
        return new int[] { 2186 };
    }

    @Override
    public int getDuration(boolean success, WorldObject object) {
        return 3;
    }

    @Override
    public double getSuccessXp(WorldObject object) {
        return 0;
    }
}
