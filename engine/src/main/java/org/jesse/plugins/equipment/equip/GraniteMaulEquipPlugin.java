package org.jesse.plugins.equipment.equip;

import org.jesse.game.item.Item;
import org.jesse.game.item.ids.ItemId;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.action.combat.PlayerCombat;

/**
 * @author Kris | 20/04/2022
 */
public class GraniteMaulEquipPlugin implements EquipPlugin {
    @Override
    public boolean handle(final Player player, final Item item, final int slotId, final int equipmentSlot) {
        player.getTemporaryAttributes().remove(PlayerCombat.QUEUED_SPECS_ATTRIBUTE);
        return true;
    }

    @Override
    public int[] getItems() {
        return new int[] {
                ItemId.GRANITE_MAUL, ItemId.GRANITE_MAUL_12848, ItemId.GRANITE_MAUL_20557, ItemId.GRANITE_MAUL_24225, ItemId.GRANITE_MAUL_24227
        };
    }
}
