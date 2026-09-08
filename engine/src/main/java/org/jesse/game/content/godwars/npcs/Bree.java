package org.jesse.game.content.godwars.npcs;

import org.jesse.game.util.Direction;
import org.jesse.game.world.Projectile;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.SoundEffect;
import org.jesse.game.world.entity.masks.Graphics;
import org.jesse.game.world.entity.masks.Hit;
import org.jesse.game.world.entity.masks.HitType;
import org.jesse.game.world.entity.npc.Spawnable;
import org.jesse.game.world.entity.npc.combat.CombatScript;

/**
 * @author Kris | 21/08/2019 01:41
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class Bree extends GodwarsBossMinion implements Spawnable, CombatScript {
    public Bree(final int id, final Location tile, final Direction facing, final int radius) {
        super(id, tile, facing, radius);
    }

    @Override
    public boolean validate(final int id, final String name) {
        return id == 2208;
    }

    private static final Projectile projectile = new Projectile(12, 52, 30, 40, 15, 10, 64, 5);
    private static final Graphics attackGraphics = new Graphics(1185);
    private static final SoundEffect attackSound = new SoundEffect(3841, 10, 0);

    @Override
    public int attack(final Entity target) {
        final Bree npc = this;
        npc.setAnimation(npc.getCombatDefinitions().getAttackAnim());
        npc.setGraphics(attackGraphics);
        delayHit(npc, World.sendProjectile(npc, target, projectile), target, new Hit(npc, getRandomMaxHit(npc, npc.getCombatDefinitions().getMaxHit(), RANGED, target), HitType.RANGED));
        World.sendSoundEffect(getMiddleLocation(), attackSound);
        return npc.getCombatDefinitions().getAttackSpeed();
    }
}
