package org.jesse.game.model.ui.testinterfaces;

import org.jesse.game.GameInterface;
import org.jesse.game.item.Item;
import org.jesse.game.model.ui.Interface;
import org.jesse.game.util.ItemUtil;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.container.impl.Trade;

/**
 * @author Tommeh | 10-3-2019 | 19:08
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class TradeInventoryInterface extends Interface {
    @Override
    protected void attach() {
        put(0, "Add Item");
    }

    @Override
    public void open(Player player) {
        player.getInterfaceHandler().sendInterface(getInterface());
    }

    @Override
    protected void build() {
        bind("Add Item", (player, slotId, itemId, option) -> {
            if (option == 10) {
                ItemUtil.sendItemExamine(player, itemId);
                return;
            }

            final Trade trade = player.getTrade();
            final Item item = player.getInventory().getItem(slotId);
            if (item == null) {
                return;
            }
            if (option == 5) {
                player.sendInputInt("Enter amount:", amount -> trade.addItem(slotId, amount));
            } else {
                final int amount = option == 1 ? 1 : option == 2 ? 5 : option == 3 ? 10 : player.getInventory().getAmountOf(item.getId());
                trade.addItem(slotId, amount);
            }
        });
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.TRADE_INVENTORY;
    }
}
