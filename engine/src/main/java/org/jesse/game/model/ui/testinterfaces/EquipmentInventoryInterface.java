package org.jesse.game.model.ui.testinterfaces;

import org.jesse.game.GameInterface;
import org.jesse.game.item.Item;
import org.jesse.game.model.ui.Interface;
import org.jesse.game.model.ui.SwitchPlugin;
import org.jesse.game.util.ItemUtil;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.container.Container;
import org.jesse.game.world.entity.player.container.impl.ContainerType;

/**
 * @author Tommeh | 25-4-2019 | 18:37
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class EquipmentInventoryInterface extends Interface implements SwitchPlugin {
    @Override
    protected void attach() {
        put(0, "Interact with Item");
    }

    @Override
    public void open(Player player) {
        player.getInterfaceHandler().sendInterface(this);
    }

    @Override
    protected void build() {
        bind("Interact with Item", (player, slotId, itemId, option) -> {
            final Item item = player.getInventory().getItem(slotId);
            if (item == null) {
                return;
            }
            if (option == 1) {
                player.getEquipment().wear(slotId);
            } else if (option == 10) {
                ItemUtil.sendItemExamine(player, item.getId());
            }
        });
        bind("Interact with Item", "Interact with Item", ((player, fromSlot, toSlot) -> {
            if (fromSlot < 0 || toSlot < 0 || fromSlot >= Container.getSize(ContainerType.INVENTORY) || toSlot >= Container.getSize(ContainerType.INVENTORY)) return;
            player.getInventory().switchItem(fromSlot, toSlot);
        }));
    }

    @Override
    public GameInterface getInterface() {
        return GameInterface.EQUIPMENT_INVENTORY;
    }
}
