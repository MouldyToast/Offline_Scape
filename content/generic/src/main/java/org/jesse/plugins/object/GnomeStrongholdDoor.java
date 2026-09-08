package org.jesse.plugins.object;

import org.jesse.game.task.WorldTask;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Kris | 24/03/2019 13:28
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class GnomeStrongholdDoor implements ObjectAction {

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (option.equalsIgnoreCase("Open")) {
            player.lock(3);
            final boolean isInside = player.getY() >= 3493;
            final int id = object.getId();
            if (!isInside) {
                player.addWalkSteps(id == 1967 ? 2465 : 2466, 3491);
            } else {
                player.addWalkSteps(id == 1967 ? 2465 : 2466, 3493);
            }
            WorldTasksManager.schedule(new WorldTask() {

                private int ticks;

                private WorldObject door;

                @Override
                public void run() {
                    switch(ticks++) {
                        case 0:
                            player.setRunSilent(3);
                            door = new WorldObject(object);
                            door.setId(door.getId() + 2);
                            World.spawnObject(door);
                            return;
                        case 1:
                            if (isInside) {
                                player.addWalkSteps(player.getX(), player.getY() - 2, 2, false);
                            } else {
                                player.addWalkSteps(player.getX(), player.getY() + 2, 2, false);
                            }
                            return;
                        case 4:
                            World.spawnObject(object);
                            stop();
                    }
                }
            }, 0, 0);
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.TREE_DOOR, ObjectId.TREE_DOOR_1968 };
    }
}
