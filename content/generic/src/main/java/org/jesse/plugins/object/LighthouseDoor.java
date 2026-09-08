package org.jesse.plugins.object;

import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Kris | 15/04/2019 21:54
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class LighthouseDoor implements ObjectAction {

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        final Location destination = player.getY() <= 3635 ? new Location(2509, 3636, 0) : new Location(2509, 3635, 0);
        player.lock(3);
        final WorldObject obj = new WorldObject(object);
        obj.setId(4578);
        World.spawnObject(obj);
        player.addWalkSteps(destination.getX(), destination.getY(), 1, false);
        WorldTasksManager.schedule(() -> World.spawnObject(object), 1);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.DOORWAY_4577 };
    }
}
