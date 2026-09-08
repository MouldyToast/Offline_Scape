package org.jesse.game.model.ui.testinterfaces;

import org.jesse.game.GameInterface;
import org.jesse.game.item.Item;
import org.jesse.game.model.ui.Interface;
import org.jesse.game.util.ItemUtil;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.container.impl.Trade;
import org.jesse.game.world.entity.player.container.impl.TradeStatus;

import java.util.Optional;

/**
 * @author Tommeh | 10-3-2019 | 18:59
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class TradeStage1Interface extends Interface {
    @Override
    protected void attach() {
        put(10, "Accept");
        put(25, "Remove Item");
    }

    @Override
    public void open(Player player) {
        player.getInterfaceHandler().sendInterface(getInterface());
    }

    @Override
    public void close(final Player player, final Optional<GameInterface> replacement) {
        if (!replacement.isPresent() || !replacement.get().equals(GameInterface.TRADE_STAGE2)) {
            player.getTrade().closeTrade(TradeStatus.CANCEL);
            player.getInterfaceHandler().closeInterfaces();
        }
    }

    @Override
    protected void build() {
        bind("Accept", player -> {
            player.getTrade().accept(1);
        });
        bind("Remove Item", (player, slotId, itemId, option) -> {
            if (option == 10) {
                ItemUtil.sendItemExamine(player, itemId);
                return;
            }
            final Trade trade = player.getTrade();
            final Item item = trade.getContainer().get(slotId);
            if (item == null) {
                return;
            }
            if (option == 5) {
                player.sendInputInt("Enter amount:", amount -> trade.removeItem(slotId, amount));
            } else {
                final int amount = option == 1 ? 1 : option == 2 ? 5 : option == 3 ? 10 : trade.getContainer().getAmountOf(item.getId());
                trade.removeItem(slotId, amount);
            }
        });
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.TRADE_STAGE1;
    }
}
