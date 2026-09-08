package org.jesse.game.content.chompy.plugins;

import org.jesse.game.item.Item;
import org.jesse.game.model.item.ItemChain;
import org.jesse.game.model.item.ItemOnNPCAction;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.player.Player;
import org.apache.commons.lang3.ArrayUtils;

public class BellowsOnSwampToad implements ItemOnNPCAction {
    @Override
    public void handleItemOnNPCAction(Player player, Item item, int slot, NPC npc) {
        SwampToad.start(player, npc, item, slot);
    }

    @Override
    public Object[] getItems() {
        return ArrayUtils.toObject(ItemChain.OGRE_BELLOWS.getAllButFirst());
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { SwampToad.SWAMP_TOAD_ID };
    }
}
