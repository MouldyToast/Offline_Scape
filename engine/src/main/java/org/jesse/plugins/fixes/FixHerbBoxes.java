package org.jesse.plugins.fixes;

import com.google.common.eventbus.Subscribe;
import org.jesse.game.item.Item;
import org.jesse.game.item.ids.ItemId;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.container.impl.Inventory;
import org.jesse.game.world.entity.player.container.impl.bank.Bank;
import org.jesse.plugins.events.PostInitializationEvent;

public class FixHerbBoxes {

    @Subscribe
    public static void onLoginEvent(PostInitializationEvent event) {
        Player player = event.getPlayer();
        if(player != null) {
            Bank bank = player.getBank();
            if(bank != null && bank.containsItem(new Item(ItemId.HERB_BOX, 500))) {
                int herbBoxCount = bank.getAmountOf(ItemId.HERB_BOX);
                if(herbBoxCount > 0)
                    bank.remove(new Item(ItemId.HERB_BOX, herbBoxCount));
            }
            if(bank != null && bank.containsItem(new Item(ItemId.OPEN_HERB_BOX, 500))) {
                int herbBoxCount = bank.getAmountOf(ItemId.OPEN_HERB_BOX);
                if(herbBoxCount > 0)
                    bank.remove(new Item(ItemId.OPEN_HERB_BOX, herbBoxCount));
            }


            Inventory inventory = player.getInventory();
            if(inventory != null && inventory.containsItem(new Item(ItemId.HERB_BOX, 500))) {
                int herbBoxCount = inventory.getAmountOf(ItemId.HERB_BOX);
                if(herbBoxCount > 0)
                    inventory.deleteItem(new Item(ItemId.HERB_BOX, herbBoxCount));
            }
            if(inventory != null && inventory.containsItem(new Item(ItemId.OPEN_HERB_BOX, 500))) {
                int herbBoxCount = inventory.getAmountOf(ItemId.OPEN_HERB_BOX);
                if(herbBoxCount > 0)
                    inventory.deleteItem(new Item(ItemId.OPEN_HERB_BOX, herbBoxCount));
            }

        }
    }
}
