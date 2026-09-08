package org.jesse.plugins.object;

import org.jesse.game.world.entity.ImmutableLocation;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Christopher
 * @since 3/13/2020
 */
public class HouseOnHillStairs implements ObjectAction {
    private static final ImmutableLocation HOUSE_TOP = new ImmutableLocation(3757, 3869, 1);
    private static final ImmutableLocation HOUSE_BOTTOM = new ImmutableLocation(3753, 3869, 0);

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        final ImmutableLocation destination = object.getId() == ObjectId.STAIRS_30681 ? HOUSE_TOP : HOUSE_BOTTOM;
        player.setLocation(destination);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] {ObjectId.STAIRS_30681, ObjectId.STAIRS_30682};
    }
}
