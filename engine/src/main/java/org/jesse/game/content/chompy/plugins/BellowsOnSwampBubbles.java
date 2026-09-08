package org.jesse.game.content.chompy.plugins;

import org.jesse.game.content.chompy.BellowsAction;
import org.jesse.game.item.Item;
import org.jesse.game.model.item.ItemChain;
import org.jesse.game.model.item.ItemOnObjectAction;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.WorldObject;
import org.apache.commons.lang3.ArrayUtils;

public class BellowsOnSwampBubbles implements ItemOnObjectAction {
    @Override
    public void handleItemOnObjectAction(Player player, Item item, int slot, WorldObject object) {
        player.getActionManager().setAction(new BellowsAction());
    }

    @Override
    public Object[] getItems() {
        return ArrayUtils.toObject(ItemChain.OGRE_BELLOWS.getAllButLast());
    }

    @Override
    public Object[] getObjects() {
        return new Object[]{SwampBubbles.SWAMP_BUBBLES_ID};
    }
}
