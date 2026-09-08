package org.jesse.game.content.skills.agility.shortcut;

import org.jesse.game.content.skills.agility.Shortcut;
import org.jesse.game.task.WorldTask;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.masks.ForceMovement;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.WorldObject;

public class ShiloVillageSteppingStone implements Shortcut {

    private static final Location NORTH = new Location(2863, 2976, 0);
    private static final Location SOUTH = new Location(2863, 2971, 0);

    @Override
    public void startSuccess(Player player, WorldObject object) {
        final boolean north = player.getY() > object.getY();
        WorldTasksManager.schedule(new WorldTask() {

            private int ticks;

            @Override
            public void run() {
                if(ticks == 0) {
                    player.setAnimation(Animation.JUMP);
                    player.setForceMovement(new ForceMovement(player.getLocation(), 15, object, 35, north ? ForceMovement.SOUTH : ForceMovement.NORTH));
                } else if(ticks == 1)
                    player.setLocation(object);
                else if(ticks == 2) {
                    player.setAnimation(Animation.JUMP);
                    player.setForceMovement(new ForceMovement(player.getLocation(), 15, north ? SOUTH : NORTH, 35, north ? ForceMovement.SOUTH : ForceMovement.NORTH));
                } else if(ticks == 3) {
                    player.setLocation(north ? SOUTH : NORTH);
                    stop();
                }

                ticks++;
            }

        },0, 0);
    }

    @Override
    public int getLevel(WorldObject object) {
        return 77;
    }

    @Override
    public int[] getObjectIds() {
        return new int[] { 16466 };
    }

    @Override
    public Location getRouteEvent(Player player, WorldObject object) {
        return player.getY() > object.getY() ? NORTH : SOUTH;
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
