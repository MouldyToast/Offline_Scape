package org.jesse.game.content.skills.farming.plugins;

import org.jesse.game.item.Item;
import org.jesse.game.model.item.ItemOnObjectAction;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.container.impl.Inventory;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Kris | 22/05/2019 00:38
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class SackFillItemOnObject implements ItemOnObjectAction {
    @Override
    public void handleItemOnObjectAction(final Player player, final Item item, final int slot, final WorldObject object) {
        final Inventory inventory = player.getInventory();
        inventory.ifDeleteItem(new Item(5418), () -> {
            inventory.addItem(new Item(6057));
            player.sendMessage("You fill the sack with straw.");
        });
    }

    @Override
    public Object[] getItems() {
        return new Object[] {5418};
    }

    @Override
    public Object[] getObjects() {
        return new Object[] {"hay bale", "haystack", "hay bales"};
    }
}
