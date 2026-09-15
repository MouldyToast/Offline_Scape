package org.jesse.game.content.godwars.npcs;

import org.jesse.game.util.Direction;
import org.jesse.game.world.Projectile;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.SoundEffect;
import org.jesse.game.world.entity.masks.Hit;
import org.jesse.game.world.entity.masks.HitType;
import org.jesse.game.world.entity.npc.Spawnable;
import org.jesse.game.world.entity.npc.combat.CombatScript;

/**
 * @author Kris | 21/08/2019 01:50
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class FlockleaderGeerin extends GodwarsBossMinion implements Spawnable, CombatScript {
    public FlockleaderGeerin(final int id, final Location tile, final Direction facing, final int radius) {
        super(id, tile, facing, radius);
    }

    @Override
    public boolean validate(final int id, final String name) {
        return id == 3164;
    }

    private static final Projectile javelin = new Projectile(1192, 396, 120, 30, 10, 3, 0, 5);
    private static final SoundEffect attackSound = new SoundEffect(2699, 10, 0);

    @Override
    public int attack(final Entity target) {
        final FlockleaderGeerin npc = this;
        npc.setAnimation(npc.getCombatDefinitions().getAttackAnim());
        World.sendSoundEffect(getMiddleLocation(), attackSound);
        delayHit(npc, World.sendProjectile(npc, target, javelin), target, new Hit(npc, getRandomMaxHit(npc, npc.getCombatDefinitions().getMaxHit(), RANGED, target), HitType.RANGED));
        return npc.getCombatDefinitions().getAttackSpeed();
    }
}
