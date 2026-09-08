package org.jesse.game.content.minigame.inferno.model;

import org.jesse.game.world.entity.player.Player;

/**
 * @author Tommeh | 02/12/2019 | 17:54
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public interface InfernoMechanics {

    default void onSpawn() { }

    default int attack(final Player player) {
        return -1;
    }

    default boolean isRevivable() { return false; }
}
