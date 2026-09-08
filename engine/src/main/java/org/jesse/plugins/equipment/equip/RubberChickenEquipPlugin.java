package org.jesse.plugins.equipment.equip;

import org.jesse.game.item.Item;
import org.jesse.game.item.ids.ItemId;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.container.Container;
import org.jetbrains.annotations.NotNull;

/**
 * @author Chris
 * @since August 18 2020
 */
public class RubberChickenEquipPlugin implements EquipPlugin {
    @Override
    public boolean handle(Player player, Item item, int slotId, int equipmentSlot) {
        return true;
    }

    @Override
    public void onEquip(final Player player, final Container container, final Item equippedItem) {
        player.setPlayerItemOnPlayerOption("Whack", true);
    }

    @Override
    public void onUnequip(final Player player, final Container container, final Item unequippedItem) {
        player.setPlayerItemOnPlayerOption("Whack", false);
    }

    @Override
    public void onLogin(@NotNull final Player player, @NotNull final Item item, final int slot) {
        player.setPlayerItemOnPlayerOption("Whack", true);
    }

    @Override
    public int[] getItems() {
        return new int[] {ItemId.RUBBER_CHICKEN};
    }
}
