package org.jesse.plugins.itemonitem;

import org.jesse.game.item.Item;
import org.jesse.game.model.item.PairedItemOnItemPlugin;
import org.jesse.game.world.entity.player.Player;

/**
 * @author Kris | 14/06/2019 11:58
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class CoconutPouring implements PairedItemOnItemPlugin {
    @Override
    public void handleItemOnItemAction(final Player player, final Item from, final Item to, final int fromSlot, final int toSlot) {
        player.getInventory().deleteItem(from);
        player.getInventory().deleteItem(to);
        player.getInventory().addItem(new Item(5978));
        player.getInventory().addItem(new Item(5935));
    }

    @Override
    public ItemPair[] getMatchingPairs() {
        return new ItemPair[] {
                ItemPair.of(5976, 229)
        };
    }
}
