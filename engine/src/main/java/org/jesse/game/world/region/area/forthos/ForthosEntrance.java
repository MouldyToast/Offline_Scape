package org.jesse.game.world.region.area.forthos;

import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Andys1814
 */
public final class ForthosEntrance implements ObjectAction {

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (object.getId() == 34865) {
            player.teleport(new Location(1800, 9968));
        } else {
            player.setAnimation(Animation.LADDER_DOWN);
            WorldTasksManager.schedule(() -> {
                player.teleport(new Location(1830, 9973));
            });
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { 34865, 34862 };
    }

}
