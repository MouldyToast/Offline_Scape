package org.jesse.game.world.region.area.plugins;

import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.action.combat.magic.CombatSpell;

/**
 * @author Tommeh | 30-11-2018 | 19:22
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public interface PlayerCombatPlugin {

    default boolean processCombat(final Player player, final Entity entity, final String style) {
        return false;
    }

    default void onAttack(final Player player, final Entity entity, final String style,
                          final CombatSpell spell, final boolean splash) {
    }

}
