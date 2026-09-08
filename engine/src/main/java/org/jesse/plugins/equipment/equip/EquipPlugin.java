package org.jesse.plugins.equipment.equip;

import org.jesse.game.item.Item;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.container.Container;
import org.jesse.plugins.Plugin;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 27. march 2018 : 4:11.53
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public interface EquipPlugin extends Plugin {

    boolean handle(final Player player, final Item item, final int slotId, final int equipmentSlot);

    default void onUnequip(final Player player, final Container container, final Item unequippedItem) {

    }

    default void onEquip(final Player player, final Container container, final Item equippedItem) {

    }

    default void onLogin(@NotNull final Player player, @NotNull final Item item, final int slot) {

    }

    int[] getItems();

}
