package org.jesse.game.world.region.area.plugins;

import org.jesse.game.item.Item;
import org.jesse.game.world.entity.player.Player;

/**
 * @author Tommeh | 1-12-2018 | 16:51
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public interface EquipmentPlugin {

    default boolean equip(final Player player, final Item item, final int slot) {
        return true;
    }

    default boolean unequip(final Player player, final Item item, final int slot) {
        return true;
    }

}
