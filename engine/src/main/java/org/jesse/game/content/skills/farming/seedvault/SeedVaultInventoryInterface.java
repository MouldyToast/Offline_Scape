package org.jesse.game.content.skills.farming.seedvault;

import org.jesse.game.GameInterface;
import org.jesse.game.model.ui.Interface;
import org.jesse.game.model.ui.SwitchPlugin;
import org.jesse.game.net.packet.PacketDispatcher;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.container.Container;
import org.jesse.game.world.entity.player.container.impl.ContainerType;

import static org.jesse.game.util.AccessMask.*;

public class SeedVaultInventoryInterface extends Interface implements SwitchPlugin {
    public static void deposit(final Player player, final int slotId, final int amount, boolean notifyOnFailure) {
        player.getSeedVault().deposit(player.getInventory().getContainer(), slotId, amount, notifyOnFailure);
        player.getSeedVault().getContainer().refresh(player);
        player.getInventory().refreshAll();
    }

    @Override
    protected void attach() {
        put(1, "Container");
    }

    @Override
    public void open(Player player) {
        player.getInterfaceHandler().sendInterface(getInterface());
        final PacketDispatcher dispatcher = player.getPacketDispatcher();
        final int inventorySize = Container.getSize(ContainerType.INVENTORY);
        dispatcher.sendComponentSettings(getInterface(), getComponent("Container"), 0, inventorySize, CLICK_OP1, CLICK_OP2, CLICK_OP3, CLICK_OP4, CLICK_OP5, CLICK_OP6, CLICK_OP7, CLICK_OP8, CLICK_OP9, CLICK_OP10, DRAG_DEPTH1, DRAG_TARGETABLE);
    }

    @Override
    protected void build() {
        bind("Container", ((player, slotId, itemId, optionId) -> {
            final SeedVaultExchangeOption option = SeedVaultExchangeOption.of(optionId);
            switch (option) {
            case SELECTED: 
                deposit(player, slotId, player.getVarManager().getValue(SeedVaultInterface.AMOUNT_VAR), true);
                break;
            case X: 
                player.sendInputInt("How much would you like to deposit?", amt -> deposit(player, slotId, amt, true));
                break;
            case EXAMINE: 
                break;
            default: 
                deposit(player, slotId, option.getAmount(), true);
                break;
            }
        }));
        bind("Container", "Container", ((player, fromSlot, toSlot) -> player.getInventory().switchItem(fromSlot, toSlot)));
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.SEED_VAULT_INVENTORY;
    }
}
