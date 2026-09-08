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
public final class ForthosExit implements ObjectAction {

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (object.getId() == 34864) {
            player.teleport(new Location(1671, 3567));
        } else {
            player.setAnimation(Animation.LADDER_UP);
            WorldTasksManager.schedule(() -> {
                player.teleport(new Location(1702, 3575));
            });
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { 34864, 34863 };
    }

}
