package org.jesse.game.world.entity.player.var;

import org.jesse.game.world.entity.player.Player;

public interface VarFunction {
    int getValue(final Player player);
    int getValue(final Player player, final int idx);
}
