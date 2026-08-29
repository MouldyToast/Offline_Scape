package com.zenyte.game.content.boons;

import com.near_reality.game.content.shop.ShopCurrencyHandler;
import com.zenyte.game.GameInterface;
import com.zenyte.game.model.shop.ShopCurrency;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.model.ui.InterfacePosition;
import com.zenyte.game.util.AccessMask;
import com.zenyte.game.world.entity.player.Player;

import static com.zenyte.game.GameInterface.PVPW_PERKS;

public class BoonPurchaseInterface extends Interface {
    @Override
    protected void attach() {
        put(8, "Buy");
        put(11, "Entry");
        put(13, "Open Exchange");
    }

    @Override
    public void open(Player player) {
        // If the account has a PIN & Required unlocking; STOP HERE
        if (player.getBankPin().requiresVerification(player, () -> open(player))) return;

        player.getVarManager().sendVarInstant(4506, ShopCurrencyHandler.getAmount(ShopCurrency.EXCHANGE_POINTS, player));
        updateAllUnlockedPerks(player);
        player.getPacketDispatcher().sendComponentSettings(getInterface(), 8, 0, 10, AccessMask.CLICK_OP1, AccessMask.CLICK_OP2, AccessMask.CLICK_OP3, AccessMask.CLICK_OP5);
        player.getPacketDispatcher().sendComponentSettings(getInterface(), 11, 0, BoonLoader.boonTypes.size() * 5, AccessMask.CLICK_OP3, AccessMask.CLICK_OP5);
        player.getInterfaceHandler().sendInterface(InterfacePosition.CENTRAL, 1621);
    }

    private void updateAllUnlockedPerks(Player player) {
        for(BoonWrapper wrapper: BoonWrapper.values()) {
            if(player.getBoonManager().hasBoon(wrapper.getPerk()) && wrapper.getId() != -1) {
                player.getVarManager().sendBitInstant(19499 + wrapper.getId(), 1);
            }
        }
    }

    @Override
    protected void build() {
        bind("Buy", (player, slotId, itemId, option) -> player.getBoonManager().purchaseBoon(BoonWrapper.get(itemId).getPerk(), itemId));
        bind("Entry", (player, slotId, itemId, option) -> player.getBoonManager().toggleBoon(BoonWrapper.get((slotId / 5) + 1).getPerk()));
        bind("Open Exchange", (player, slotId, itemId, option) -> {
            // If the account has a PIN & Required unlocking; STOP HERE
            if (player.getBankPin().requiresVerification(player, () -> GameInterface.REMNANT_EXCHANGE.open(player)))
                return;
            GameInterface.REMNANT_EXCHANGE.open(player);
            //player.getInterfaceHandler().sendInterface(GameInterface.REMNANT_EXCHANGE);
        });
    }

    @Override
    public GameInterface getInterface() {
        return PVPW_PERKS;
    }
}
