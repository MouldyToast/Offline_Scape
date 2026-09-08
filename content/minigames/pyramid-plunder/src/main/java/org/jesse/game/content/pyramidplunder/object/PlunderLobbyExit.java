package org.jesse.game.content.pyramidplunder.object;

import org.jesse.game.content.pyramidplunder.PyramidPlunderConstants;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Christopher
 * @since 4/1/2020
 */
public class PlunderLobbyExit implements ObjectAction {
    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        player.lock(1);
        player.setLocation(PyramidPlunderConstants.OUTSIDE_PYRAMID);
    }

    @Override
    public Object[] getObjects() {
        return new Object[]{ObjectId.TOMB_DOOR_20932};
    }
}
