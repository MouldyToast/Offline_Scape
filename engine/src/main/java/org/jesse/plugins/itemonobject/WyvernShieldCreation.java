package org.jesse.plugins.itemonobject;

import org.jesse.game.item.Item;
import org.jesse.game.model.item.ItemOnObjectAction;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.WorldObject;
import org.jesse.plugins.object.StrangeMachine;

public class WyvernShieldCreation implements ItemOnObjectAction {

    @Override
    public void handleItemOnObjectAction(Player player, Item item, int slot, WorldObject object) {
        StrangeMachine.start(player, object);
    }

    @Override
    public Object[] getItems() {
        return StrangeMachine.requiredItems.toArray();
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { StrangeMachine.STRANGE_MACHINE_ID };
    }
}
