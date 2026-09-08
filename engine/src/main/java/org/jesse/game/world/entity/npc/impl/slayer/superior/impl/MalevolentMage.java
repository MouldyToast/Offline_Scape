package org.jesse.game.world.entity.npc.impl.slayer.superior.impl;

import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.npc.combat.CombatScript;
import org.jesse.game.world.entity.npc.impl.slayer.superior.SuperiorNPC;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.action.combat.magic.CombatSpell;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 28/05/2019 02:07
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class MalevolentMage extends SuperiorNPC implements CombatScript {
    public MalevolentMage(@NotNull final Player owner, @NotNull final NPC root, final Location tile) {
        super(owner, root, 7396, tile);
    }

    @Override
    public int attack(final Entity target) {
        useSpell(CombatSpell.FIRE_BLAST, target, combatDefinitions.getMaxHit());
        return this.getCombatDefinitions().getAttackSpeed();
    }

}
