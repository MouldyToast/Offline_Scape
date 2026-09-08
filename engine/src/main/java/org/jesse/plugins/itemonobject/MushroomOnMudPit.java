package org.jesse.plugins.itemonobject;

import org.jesse.game.item.Item;
import org.jesse.game.item.ids.ItemId;
import org.jesse.game.model.item.ItemOnObjectAction;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.WorldObject;
import org.jesse.plugins.object.MudPit;

/**
 * @author Chris
 * @since July 20 2020
 */
public class MushroomOnMudPit implements ItemOnObjectAction {

    @Override
    public void handleItemOnObjectAction(Player player, Item item, int slot, WorldObject object) {
        MudPit.attemptFill(player);
    }

    @Override
    public Object[] getItems() {
        return new Object[] { ItemId.MUSHROOM };
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { 31426 };
    }
}
