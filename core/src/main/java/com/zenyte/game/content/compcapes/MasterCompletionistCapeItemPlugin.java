package com.zenyte.game.content.compcapes;

import com.near_reality.game.item.CustomItemId;
import com.near_reality.game.model.ui.cape_customizer.CapeCustomizerInterfacePlugin;
import com.zenyte.game.GameInterface;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.game.world.entity.player.container.impl.ContainerType;
import com.zenyte.game.world.entity.player.container.impl.equipment.EquipmentSlot;
import com.zenyte.plugins.Listener;
import com.zenyte.plugins.ListenerType;
import com.zenyte.plugins.equipment.equip.EquipPlugin;

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
        if (player.getEquipment().getId(EquipmentSlot.CAPE) == CustomItemId.MASTER_COMP_CAPE) {
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
        return new int[]{CustomItemId.MASTER_COMP_CAPE};
    }
}
