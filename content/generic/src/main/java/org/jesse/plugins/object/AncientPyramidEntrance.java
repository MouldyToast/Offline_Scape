package org.jesse.plugins.object;

import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Kris | 24/01/2019 16:55
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class AncientPyramidEntrance implements ObjectAction {

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (option.equals("Enter")) {
            player.lock(2);
            player.setAnimation(new Animation(844));
            WorldTasksManager.schedule(() -> {
                player.setLocation(new Location(3233, 9312, 0));
                WorldTasksManager.schedule(() -> player.addWalkSteps(3233, 9313));
            });
        }
    }

    public int getDelay() {
        return 1;
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.TUNNEL_6481 };
    }
}
