package org.jesse.plugins.equipment.equip;

import org.jesse.game.item.Item;
import org.jesse.game.item.ids.ItemId;
import org.jesse.game.world.entity.player.Player;

/**
 * @author Leviticus | 05/05/2025 1:35 PM
 */
public class VenatorsRingEquipPlugin implements EquipPlugin {
    @Override
    public boolean handle(Player player, Item item, int slotId, int equipmentSlot) {
        final int kills = player.getNotificationSettings().getKillcount("leviathan");
        if (kills == 0) {
            player.sendMessage("The ring slips off your finger. The power within it seems unfamiliar.");
            return false;
        }
        return true;
    }

    @Override
    public int[] getItems() {
        return new int[]{
                ItemId.VENATOR_RING_28310
        };
    }
}
