package org.jesse.game.content.breaches.entity.impl;

import org.jesse.game.content.breaches.entity.BreachEntity;
import org.jesse.game.content.skills.prayer.Prayer;
import org.jesse.game.util.Direction;
import org.jesse.game.util.Utils;
import org.jesse.game.world.Projectile;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.SoundEffect;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.masks.Graphics;
import org.jesse.game.world.entity.masks.Hit;
import org.jesse.game.world.entity.masks.HitType;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.npc.ids.NpcId;
import org.jesse.game.world.entity.npc.Spawnable;
import org.jesse.game.world.entity.npc.combat.CombatScript;
import org.jesse.game.world.entity.npc.combatdefs.AttackType;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.action.combat.CombatUtilities;
import org.jesse.game.world.entity.player.action.combat.magic.CombatSpell;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.util.ArrayList;
import java.util.List;

public class Derwen extends BreachEntity implements Spawnable, CombatScript {
    private static final Projectile energyBall = new Projectile(1512, 50, 10, 40, 10, 70, 64, 5);

    public Derwen(int id, Location tile, Direction facing, int radius) {
        super(id, tile, facing, radius);
    }

    @Override
    public boolean validate(int id, String name) {
        return id == NpcId.DERWEN_12450;
    }

    @Override
    public int attack(Entity target) {
        if (canUseSpecial(target)) {
            final Location tile = target.getLocation().transform(Utils.random(3), Utils.random(3), 0);
            setAnimation(getSpecialAnimation());
            World.scheduleProjectile(this, tile, energyBall).schedule(() -> {
                if (isDead() || isFinished()) {
                    return;
                }
                final EnergyBall ball = new EnergyBall(this, tile);
                ball.spawn();
                energyBalls.add(ball);
            });
        }
        if (isWithinMeleeDistance(this, target)) {
            setAnimation(getMeleeAnimation());
            var maxHit = CombatUtilities.getRandomMaxHit(this, 16, AttackType.CRUSH, target);
            var hit = new Hit(this, maxHit, HitType.MELEE);
            delayHit(this, 0, target, hit);
            target.setGraphics(hitGraphics());
        }
        else {
            setAnimation(getMagicAnimation());
            final CombatSpell spell = validSpell();
            final int delay = 0;
            final int clientDelay = 30;
            final SoundEffect sound = spell.getHitSound();
            final Graphics gfx = hitGraphics();
            final Hit hit = magic(target, 43);
            final boolean splash = hit.getDamage() <=
                ((target instanceof Player player && player.getPrayerManager().isActive(Prayer.PROTECT_FROM_MAGIC) ? 1 : 0));

            World.sendSoundEffect(target.getLocation(), splash ?
                new SoundEffect(227, 10, clientDelay) :
                new SoundEffect(sound.getId(), sound.getRadius(), clientDelay));

            target.setGraphics(splash ?
                new Graphics(85, clientDelay, 124) :
                new Graphics(gfx.getId(), clientDelay, gfx.getHeight()));
            delayHit(delay, target, hit);
        }
        return 6;
    }


    private static final class EnergyBall extends NPC implements CombatScript {
        private static final Projectile healProjectile = new Projectile(1513, 10, 40, 0, 10, 70, 64, 5);

        private EnergyBall(final Derwen derwen, final Location tile) {
            super(7514, tile, Direction.SOUTH, 0);
            freeze(Integer.MAX_VALUE);
            spawned = true;
            this.derwen = derwen;
        }

        @Override
        public boolean isForceAttackable() {
            return true;
        }

        private final Derwen derwen;
        private int ticks;

        @Override
        public void setAnimation(final Animation animation) {
        }

        @Override
        public void setUnprioritizedAnimation(final Animation animation) {
        }

        @Override
        public void onFinish(final Entity source) {
            super.onFinish(source);
            derwen.energyBalls.remove(this);
        }

        @Override
        public void processNPC() {
            super.processNPC();
            if (ticks++ % 8 == 0 && ticks != 1) {
                World.scheduleProjectile(this, derwen, healProjectile).schedule(() -> derwen.applyHit(new Hit(5, HitType.HEALED)));
            }
        }

        @Override
        public void applyHit(final Hit hit) {
            super.applyHit(hit);
            if (hit.getWeapon() != CombatSpell.CLAWS_OF_GUTHIX) {
                hit.setDamage(0);
            }
        }

        @Override
        public float getXpModifier(final Hit hit) {
            if (hit.getWeapon() != CombatSpell.CLAWS_OF_GUTHIX) {
                return 0;
            }
            return 1;
        }

        @Override
        public int attack(final Entity target) {
            return Integer.MAX_VALUE;
        }
    }

    private static final Animation magicAnimation = new Animation(7849);
    private static final Animation meleeAnimation = new Animation(7848);

    Graphics hitGraphics() {
        return new Graphics(1511, 0, 96);
    }

    @Override
    public void onFinish(final Entity source) {
        super.onFinish(source);
        for (final EnergyBall ball : new ArrayList<>(energyBalls)) {
            ball.finish();
        }
    }

    private final List<EnergyBall> energyBalls = new ObjectArrayList<>();

    Animation getMagicAnimation() {
        return magicAnimation;
    }

    Animation getMeleeAnimation() {
        return meleeAnimation;
    }

    Animation getSpecialAnimation() {
        return magicAnimation;
    }

    CombatSpell validSpell() {
        return CombatSpell.CLAWS_OF_GUTHIX;
    }

    boolean canUseSpecial(final Entity target) {
        return energyBalls.isEmpty();
    }

}
