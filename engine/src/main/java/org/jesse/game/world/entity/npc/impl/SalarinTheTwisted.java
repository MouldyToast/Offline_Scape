package org.jesse.game.world.entity.npc.impl;

import org.jesse.game.util.Direction;
import org.jesse.game.util.Utils;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.masks.Hit;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.npc.ids.NpcId;
import org.jesse.game.world.entity.npc.Spawnable;
import org.jesse.game.world.entity.player.action.combat.magic.CombatSpell;

/**
 * @author Kris | 15/09/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class SalarinTheTwisted extends NPC implements Spawnable {
    public SalarinTheTwisted(int id, Location tile, Direction facing, int radius) {
        super(id, tile, facing, radius);
        this.maxDistance = 8;
    }

    @Override
    public boolean validate(int id, String name) {
        return id == NpcId.SALARIN_THE_TWISTED;
    }

    @Override
    public float getXpModifier(final Hit hit) {
        final Object weapon = hit.getWeapon();
        if (weapon == CombatSpell.WIND_STRIKE || weapon == CombatSpell.EARTH_STRIKE || weapon == CombatSpell.WATER_STRIKE || weapon == CombatSpell.FIRE_STRIKE) {
            if (hit.getDamage() > 0) {
                hit.setDamage(Math.min(getHitpoints(), ((CombatSpell) weapon).getMaxHit()));
            }
            return 1;
        }
        hit.setDamage(0);
        if (Utils.random(4) == 0) {
            setForceTalk("Your pitiful attacks cannot hurt me!");
        }
        return 1;
    }
}
