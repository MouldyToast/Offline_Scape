package org.jesse.game.world.region.area.plugins;

import org.jesse.game.item.Item;
import org.jesse.game.world.entity.player.Player;

public interface IDropPlugin {

    boolean drop(final Player player, final Item item);
    boolean dropOnGround(final Player player, final Item item);

    int visibleTicks(final Player player, final Item item);
    int invisibleTicks(final Player player, final Item item);
}
