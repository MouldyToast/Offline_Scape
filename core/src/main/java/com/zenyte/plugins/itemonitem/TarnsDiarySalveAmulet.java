package com.zenyte.plugins.itemonitem;

import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.PairedItemOnItemPlugin;
import com.zenyte.game.world.entity.player.Player;

import static com.zenyte.game.item.ItemId.*;

/**
 * @author Kris | 20/06/2019 22:47
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class TarnsDiarySalveAmulet implements PairedItemOnItemPlugin {
    @Override
    public void handleItemOnItemAction(final Player player, final Item from, final Item to, final int fromSlot, final int toSlot) {
        final Item book = from.getId() == TARNS_DIARY ? from : to;
        final Item salve = from == book ? to : from;
        player.getInventory().deleteItem(book);
        player.getInventory().deleteItem(salve);
        player.getInventory().addOrDrop(new Item(salve.getId() == SALVE_AMULET ? SALVE_AMULET_E : SALVE_AMULETEI));
    }

    @Override
    public ItemPair[] getMatchingPairs() {
        return new ItemPair[] {
            ItemPair.of(SALVE_AMULET, TARNS_DIARY),
            ItemPair.of(SALVE_AMULET_E, TARNS_DIARY)
        };
    }
}
