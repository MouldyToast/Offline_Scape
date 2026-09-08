package com.zenyte.plugins.object;

import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.obj.ids.ObjectId;
import com.zenyte.game.world.object.WorldObject;

/**
 * @author Zeighe
 * @Date 1/12/2025
 */

public class ScarEssenceMineCaveObject implements ObjectAction {

    private static final Location INSIDE_LOCATION = new Location(1953, 6349, 0);

    private static final Location OUTSIDE_LOCATION = new Location(2041, 6424, 0);

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        player.setLocation(object.getId() == ObjectId.CLOSED_PASSAGE ? INSIDE_LOCATION : OUTSIDE_LOCATION);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.CLOSED_PASSAGE, ObjectId.PASSAGE_49922 };
    }
}
