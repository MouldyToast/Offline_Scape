package org.jesse.game.content.compcapes;

import org.jesse.game.item.ids.ItemId;
import org.jesse.game.model.ui.cape_customizer.CapeCustomizerInterfacePlugin;
import org.jesse.game.GameInterface;
import org.jesse.game.item.Item;
import org.jesse.game.model.item.pluginextensions.ItemPlugin;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.container.Container;
import org.jesse.game.world.entity.player.container.impl.ContainerType;
import org.jesse.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import org.jesse.plugins.Listener;
import org.jesse.plugins.ListenerType;
import org.jesse.plugins.equipment.equip.EquipPlugin;

/**
 * @author <a href="https://github.com/heavens">mack</a>
 */
public class MasterCompletionistCapeItemPlugin extends ItemPlugin implements EquipPlugin {

    @Override
    public void handle() {
        bind("Wear", ((player, item, container, slotId) -> {
            if (player.getEquipment().wear(slotId)) {
                CapeCustomizerInterfacePlugin.transmitCapeRecolors(player);
            }
        }));
        setDefault("Remove", (player, item, slotId) -> {
            player.stopAll(false, !player.getInterfaceHandler().isVisible(GameInterface.EQUIPMENT_STATS.getId()), true); //TODO improve this
            if (player.getEquipment().unequipItem(slotId)) {
                player.getAppearance().clearOverrides(true);
            }
        });
        bind("Recolour", (player, item, inv, slotId) -> {
            if (inv.getType() == ContainerType.EQUIPMENT) {
                GameInterface.CAPE_CUSTOMIZER.open(player);
            }
        });
    }

    @Listener(type = ListenerType.LOBBY_CLOSE)
    private static void onLogin(final Player player) {
        if (player.getEquipment().getId(EquipmentSlot.CAPE) == ItemId.MASTER_COMP_CAPE) {
            CapeCustomizerInterfacePlugin.transmitCapeRecolors(player);
        }
    }

    @Override
    public boolean handle(Player player, Item item, int slotId, int equipmentSlot) {
        return true;
    }

    @Override
    public void onUnequip(Player player, Container container, Item unequippedItem) {
        player.getAppearance().clearOverrides(true);
    }

    @Override
    public int[] getItems() {
        return new int[]{ItemId.MASTER_COMP_CAPE};
    }
}
