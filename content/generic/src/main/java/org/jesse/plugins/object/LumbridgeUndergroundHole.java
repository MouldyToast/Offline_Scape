package org.jesse.plugins.object;

import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Kris | 16/01/2019 02:00
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class LumbridgeUndergroundHole implements ObjectAction {

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (option.equals("Squeeze-through")) {
            player.lock(1);
            player.sendMessage("You squeeze through the hole.");
            WorldTasksManager.schedule(() -> player.setLocation(player.inArea("Lumbridge Swamp Caves") ? new Location(3219, 9618, 0) : new Location(3221, 9618, 0)));
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { 6898, 6899 };
    }
}
