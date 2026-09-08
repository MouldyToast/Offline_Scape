package org.jesse.game.world.region.area.plugins;

import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.masks.Hit;
import org.jesse.game.world.entity.player.Player;

/**
 * @author Kris | 06/07/2019 18:36
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public interface HitProcessPlugin {

    boolean hit(final Player source, final Entity target, final Hit hit, final float modifier);

}
