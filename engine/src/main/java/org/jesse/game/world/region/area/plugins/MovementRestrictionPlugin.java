package org.jesse.game.world.region.area.plugins;

import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.player.Player;

public interface MovementRestrictionPlugin {

    boolean canMoveToLocation(final Player player, final Location destination);
}
