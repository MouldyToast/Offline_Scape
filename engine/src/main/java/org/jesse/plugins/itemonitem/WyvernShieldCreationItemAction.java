package org.jesse.plugins.itemonitem;

import org.jesse.game.item.Item;
import org.jesse.game.model.item.ItemOnItemAction;
import org.jesse.game.world.entity.player.Player;
import org.jesse.plugins.object.StrangeMachine;

public class WyvernShieldCreationItemAction implements ItemOnItemAction {
    @Override
    public void handleItemOnItemAction(Player player, Item from, Item to, int fromSlot, int toSlot) {
        player.sendMessage("Perhaps some of the magical apparatus on Fossil Island can help join these two items.");
    }

    @Override
    public int[] getItems() {
        return StrangeMachine.requiredItems.toIntArray();
    }
}
