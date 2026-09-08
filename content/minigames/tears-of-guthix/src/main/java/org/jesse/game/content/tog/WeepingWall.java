package org.jesse.game.content.tog;

import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Chris
 * @since September 08 2020
 */
public class WeepingWall implements ObjectAction {
    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        final TearsOfGuthixWall togWall = TearsOfGuthixWall.of(object.getPosition());
        player.getActionManager().setAction(new CollectTearAction(togWall, object));
    }

    @Override
    public Object[] getObjects() {
        return new Object[] {ObjectId.WEEPING_WALL};
    }
}
