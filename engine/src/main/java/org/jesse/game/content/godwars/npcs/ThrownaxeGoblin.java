package org.jesse.game.content.godwars.npcs;

import org.jesse.game.util.Direction;
import org.jesse.game.world.Projectile;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.masks.Hit;
import org.jesse.game.world.entity.masks.HitType;
import org.jesse.game.world.entity.npc.Spawnable;
import org.jesse.game.world.entity.npc.combat.CombatScript;

/**
 * @author Kris | 21/08/2019 00:33
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ThrownaxeGoblin extends SpawnableKillcountNPC implements Spawnable, CombatScript {
    protected ThrownaxeGoblin(final int id, final Location tile, final Direction facing, final int radius) {
        super(id, tile, facing, radius);
    }

    @Override
    public boolean validate(final int id, final String name) {
        return id == 2249;
    }

    private static final Projectile PROJ = new Projectile(1197, 16, 22, 30, 15, 10, 64, 5);

    @Override
    public int attack(final Entity target) {
        final ThrownaxeGoblin npc = this;
        npc.setAnimation(npc.getCombatDefinitions().getAttackAnim());
        World.sendProjectile(npc, target, PROJ);
        delayHit(npc, World.sendProjectile(npc, target, PROJ), target, new Hit(npc, getRandomMaxHit(npc, npc.getCombatDefinitions().getMaxHit(), RANGED, target), HitType.RANGED));
        return npc.getCombatDefinitions().getAttackSpeed();
    }
}
