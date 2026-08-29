package com.zenyte.plugins.object.memberzones;

import com.near_reality.game.item.CustomObjectId;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.object.ObjectAction;
import com.zenyte.game.world.object.WorldObject;

public final class BlackChinDungeonObject implements ObjectAction {

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        player.lock(1);
        player.setLocation(new Location(1627, 4432, 0));
    }

    @Override
    public Object[] getObjects() {
        return new Object[]{ CustomObjectId.DONATOR_ISLAND_UDI_CHIN_DUNG};
    }
}
