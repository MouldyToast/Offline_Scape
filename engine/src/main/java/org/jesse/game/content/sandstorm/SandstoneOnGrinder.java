package org.jesse.game.content.sandstorm;

import org.jesse.game.item.Item;
import org.jesse.game.model.item.ItemOnObjectAction;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Chris
 * @since August 20 2020
 */
public class SandstoneOnGrinder implements ItemOnObjectAction {
    @Override
    public void handleItemOnObjectAction(Player player, Item item, int slot, WorldObject object) {
        Grinder.deposit(player);
    }

    @Override
    public Object[] getItems() {
        return Sandstone.SANDSTONE_IDS.toArray();
    }

    @Override
    public Object[] getObjects() {
        return new Object[] {ObjectId.GRINDER};
    }
}
