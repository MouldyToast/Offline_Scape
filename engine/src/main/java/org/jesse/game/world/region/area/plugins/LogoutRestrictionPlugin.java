package org.jesse.game.world.region.area.plugins;

import org.jesse.game.world.entity.player.Player;

public interface LogoutRestrictionPlugin {

    boolean manualLogout(final Player player);
}
