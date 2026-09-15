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
 * @author Kris | 21/08/2019 01:43
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class Growler extends GodwarsBossMinion implements Spawnable, CombatScript {
    public Growler(final int id, final Location tile, final Direction facing, final int radius) {
        super(id, tile, facing, radius);
    }

    @Override
    public boolean validate(final int id, final String name) {
        return id == 2207;
    }

    private static final Projectile projectile = new Projectile(1183, 16, 20, 30, 5, 38, 0, 5);
    private static final Graphics attackGraphics = new Graphics(1182);
    private static final Graphics targetGraphics = new Graphics(1184, -1, 0);
    private static final Graphics splashGraphics = new Graphics(85, -1, 124);
    private static final SoundEffect castSound = new SoundEffect(3863, 10, 0);
    private static final SoundEffect hitSound = new SoundEffect(3873, 10, -1);
    private static final SoundEffect splashSound = new SoundEffect(227, 10, -1);

    @Override
    public int attack(final Entity target) {
        final Growler npc = this;
        npc.setAnimation(npc.getCombatDefinitions().getAttackAnim());
        npc.setGraphics(attackGraphics);
        final Hit hit = new Hit(npc, getRandomMaxHit(npc, npc.getCombatDefinitions().getMaxHit(), MAGIC, target), HitType.MAGIC);
        delayHit(npc, World.sendProjectile(npc, target, projectile), target, hit);
        World.sendSoundEffect(getMiddleLocation(), castSound);
        final int delay = projectile.getProjectileDuration(getMiddleLocation(), target);
        if (hit.getDamage() == 0) {
            target.setGraphics(new Graphics(splashGraphics.getId(), delay, splashGraphics.getHeight()));
            World.sendSoundEffect(new Location(target.getLocation()), splashSound.withDelay(delay));
        } else {
            target.setGraphics(new Graphics(targetGraphics.getId(), delay, targetGraphics.getHeight()));
            World.sendSoundEffect(new Location(target.getLocation()), hitSound.withDelay(delay));
        }
        return npc.getCombatDefinitions().getAttackSpeed();
    }
}
