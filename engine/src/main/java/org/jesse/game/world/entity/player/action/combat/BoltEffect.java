package org.jesse.game.world.entity.player.action.combat;

import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.masks.Hit;
import org.jesse.game.world.entity.player.Player;

public interface BoltEffect {

	double applyBoltEffect(final Player player, final Entity target, final Hit hit, boolean apply);
	
}
