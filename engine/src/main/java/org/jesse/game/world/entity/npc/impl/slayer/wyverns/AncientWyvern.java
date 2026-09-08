package org.jesse.game.world.entity.npc.impl.slayer.wyverns;

import org.jesse.game.util.Direction;
import org.jesse.game.util.Utils;
import org.jesse.game.world.Projectile;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.masks.Graphics;
import org.jesse.game.world.entity.masks.Hit;
import org.jesse.game.world.entity.masks.HitType;

/**
 * @author Christopher
 * @since 2/28/2020
 */
public class AncientWyvern extends Wyvern {
    private static final Graphics icyBreathGfx = new Graphics(501, 0, 234);
    private static final Animation meleeAnim = new Animation(7651);
    private static final Animation rangedAnim = new Animation(7657);
    private static final Projectile rangedProjectile = new Projectile(500, 94, 35, 50, 0, 8, 0, 5);

    public AncientWyvern(int id, Location tile, Direction facing, int radius) {
        super(id, tile, facing, radius);
    }

    @Override
    public int attack(Entity target) {
        final int style = Utils.random(isWithinMeleeDistance(this, target) ? 2 : 1);
        if (style == 0) {
            magicAttack(target);
        } else if (style == 1) {
            setAnimation(rangedAnim);
            delayHit(this, World.sendProjectile(this, target, rangedProjectile), target, new Hit(this, getRandomMaxHit(this, 10, RANGED, target), HitType.DEFAULT));
        } else {
            setAnimation(meleeAnim);
            delayHit(this, 0, target, new Hit(this, getRandomMaxHit(this, 16, MELEE, target), HitType.MELEE));
        }
        return getCombatDefinitions().getAttackSpeed();
    }

    @Override
    public boolean validate(int id, String name) {
        return name.equalsIgnoreCase("ancient wyvern");
    }

    @Override
    protected Graphics getIcyBreathGfx() {
        return icyBreathGfx;
    }
}
