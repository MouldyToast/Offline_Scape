package org.jesse.plugins.itemonitem;

import org.jesse.game.item.Item;
import org.jesse.game.item.ids.ItemId;
import org.jesse.game.model.item.ItemOnItemAction;
import org.jesse.game.world.entity.player.Player;

/**
 * @author Chris
 * @since August 18 2020
 */
public class TorstolOnAntiVenom1To3 implements ItemOnItemAction {
    @Override
    public void handleItemOnItemAction(Player player, Item from, Item to, int fromSlot, int toSlot) {
        player.sendMessage("The torstol must be added to a 4-dose vial.");
    }

    @Override
    public ItemPair[] getMatchingPairs() {
        return new ItemPair[] {
                ItemPair.of(ItemId.TORSTOL, ItemId.ANTIVENOM1),
                ItemPair.of(ItemId.TORSTOL, ItemId.ANTIVENOM2),
                ItemPair.of(ItemId.TORSTOL, ItemId.ANTIVENOM3)
        };
    }

    @Override
    public int[] getItems() {
        return null;
    }
}
