package org.jesse.plugins.object;

import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Chris
 * @since June 09 2020
 */
public class PollnivneachBrokenLadder implements ObjectAction {
    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        player.sendMessage("This ladder seems to be broken.");
    }

    @Override
    public Object[] getObjects() {
        return new Object[] {ObjectId.LADDER_17028};
    }
}
