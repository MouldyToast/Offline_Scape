package org.jesse.game.world.region;

import org.jesse.game.world.entity.player.Player;

public interface PlayerPredicate {

	boolean test(final Player player);
	
}
