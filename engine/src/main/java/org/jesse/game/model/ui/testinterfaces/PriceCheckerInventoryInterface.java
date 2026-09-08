package org.jesse.game.model.ui.testinterfaces;

import org.jesse.game.GameInterface;
import org.jesse.game.item.Item;
import org.jesse.game.model.ui.Interface;
import org.jesse.game.model.ui.InterfacePosition;
import org.jesse.game.net.packet.PacketDispatcher;
import org.jesse.game.util.AccessMask;
import org.jesse.game.util.ItemUtil;
import org.jesse.game.world.entity.player.Player;

/**
 * @author Kris | 11/05/2019 20:30
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class PriceCheckerInventoryInterface extends Interface {
    @Override
    protected void attach() {
        put(0, "Interact with item");
    }

    @Override
    public void open(final Player player) {
        if (!player.getInterfaceHandler().isPresent(GameInterface.PRICE_CHECKER)) {
            throw new IllegalStateException("Cannot open price checker inventory overlay without price checker itself.");
        }
        final PacketDispatcher packetDispatcher = player.getPacketDispatcher();
        player.getInterfaceHandler().sendInterface(InterfacePosition.SINGLE_TAB, getId());
        packetDispatcher.sendClientScript(149, 15597568, 93, 4, 7, 0, -1, "Add<col=ff9040>", "Add-5<col=ff9040>", "Add-10<col=ff9040>", "Add-All<col=ff9040>", "Add-X<col=ff9040>");
        packetDispatcher.sendComponentSettings(getId(), getComponent("Interact with item"), 0, 27, AccessMask.CLICK_OP1, AccessMask.CLICK_OP2, AccessMask.CLICK_OP3, AccessMask.CLICK_OP4, AccessMask.CLICK_OP5, AccessMask.CLICK_OP10);
    }

    @Override
    protected void build() {
        bind("Interact with item", (player, slotId, itemId, option) -> {
            final Item item = player.getInventory().getItem(slotId);
            if (item == null) {
                return;
            }
            if (item.getId() != itemId) {
                return;
            }
            if (option == 10) {
                ItemUtil.sendItemExamine(player, item);
                return;
            }
            if (option == 5) {
                player.sendInputInt("Enter amount:", amount -> player.getPriceChecker().deposit(player, player.getInventory().getContainer(), slotId, amount));
                return;
            }
            final int amount = option == 1 ? 1 : option == 2 ? 5 : option == 3 ? 10 : Integer.MAX_VALUE;
            player.getPriceChecker().deposit(player, player.getInventory().getContainer(), slotId, amount);
        });
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.PRICE_CHECKER_INVENTORY;
    }
}
