package org.jesse.game.model.item.pluginextensions;

import org.jesse.game.item.Item;
import org.jesse.game.world.entity.player.Player;

public interface ItemOnItemExtension {

    void handleItemOnItemAction(final Player player, final Item from, final Item to, final int fromSlot, final int toSlot);

}
