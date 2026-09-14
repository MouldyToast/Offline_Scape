package org.jesse.game.content.breaches.entity.impl;

import org.jesse.game.content.breaches.entity.BreachEntity;
import org.jesse.game.util.Direction;
import org.jesse.game.util.Utils;
import org.jesse.game.world.Projectile;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.masks.Hit;
import org.jesse.game.world.entity.masks.HitType;
import org.jesse.game.world.entity.npc.Spawnable;
import org.jesse.game.world.entity.npc.combat.CombatScript;

import static org.jesse.game.npc.ids.NpcId.GENERAL_GRAARDOR_12444;

public class GeneralGraardor extends BreachEntity implements Spawnable, CombatScript {

    public GeneralGraardor(int id, Location tile, Direction facing, int radius) {
        super(id, tile, facing, radius);
    }

    @Override
    public boolean validate(final int id, final String name) {
        return id == GENERAL_GRAARDOR_12444;
    }

    private static final Projectile projectile = new Projectile(1202, 164, 64, 30, 5, 10, 0, 5);
    private static final Animation meleeAnimation = new Animation(7018);
    private static final Animation rangedAnimation = new Animation(7021);

    @Override
    public int attack(final Entity target) {
        if (Utils.randomBoolean(2)) {
            setAnimation(rangedAnimation);
            for (final Entity t : getPossibleTargets(EntityType.PLAYER)) {
                int damage = getRandomMaxHit(this, 35, RANGED, t);
                if (damage > 0)
                    damage = Utils.random(15, 35);
                delayHit(this, World.sendProjectile(this, t, projectile), t, new Hit(this, damage, HitType.RANGED));
            }
        }
        else {
            setAnimation(meleeAnimation);
            delayHit(this, 0, target, new Hit(this, getRandomMaxHit(this, getCombatDefinitions().getMaxHit(), MELEE, target), HitType.MELEE));
        }
        return getCombatDefinitions().getAttackSpeed();
    }
}
