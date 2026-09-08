package org.jesse.game.world.region.area.plugins;

import org.jesse.game.world.Position;

/**
 * @author Kris | 09/01/2019 18:21
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public interface CannonRestrictionPlugin {

    default String restrictionMessage() {
        return "You can't place a cannon here.";
    }

    default boolean canPlaceCannon(Position position) {
        return false;
    }

}
