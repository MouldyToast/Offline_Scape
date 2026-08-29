package com.zenyte.game.content.breaches.entity.impl;

import com.zenyte.game.content.breaches.entity.BreachEntity;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.DirectionUtil;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.ForceTalk;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.SoundEffect;
import com.zenyte.game.world.entity.masks.*;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.npc.combat.CombatScript;
import com.zenyte.game.world.entity.npc.combatdefs.AttackType;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.action.combat.CombatUtilities;
import com.zenyte.game.world.entity.player.action.combat.magic.CombatSpell;

public class JusticiarZachariah extends BreachEntity implements Spawnable, CombatScript {
    private static final Animation magicAnimation = new Animation(7962);
    private static final Animation meleeAnimation = new Animation(7853);
    private static final Projectile specialProjectile = new Projectile(1515, 50, 10, 40, 10, 70, 64, 5);

    public JusticiarZachariah(int id, Location tile, Direction facing, int radius) {
        super(id, tile, facing, radius);
    }

    Animation getMagicAnimation() {
        return magicAnimation;
    }

    int meleeSpeed() {
        return 3;
    }

    Animation getMeleeAnimation() {
        return meleeAnimation;
    }

    boolean canUseSpecial(final Entity target) {
        return !isWithinMeleeDistance(this, target);
    }

    Animation getSpecialAnimation() {
        return meleeAnimation;
    }

    CombatSpell validSpell() {
        return CombatSpell.SARADOMIN_STRIKE;
    }

    int meleeMax() {
        return 26;
    }

    AttackType meleeAttackType() {
        return AttackType.SLASH;
    }

    private int special(final Entity target) {
        final Location tile = new Location(target.getLocation());
        final int delay = World.sendProjectile(this, tile, specialProjectile);
        setAnimation(getSpecialAnimation());
        WorldTasksManager.schedule(() -> {
            if (target.isDead() || target.isFinished() || !target.getLocation().matches(tile)) {
                return;
            }
            final CombatSpell spell = CombatSpell.WATER_BLAST;
            final Graphics graphics = spell.getHitGfx();
            final SoundEffect sound = spell.getHitSound();
            target.setGraphics(new Graphics(graphics.getId(), 0, graphics.getHeight()));
            World.sendSoundEffect(new Location(target.getLocation()), new SoundEffect(sound.getId(), sound.getRadius(), 0));
            target.setForceTalk(new ForceTalk("Nooo!"));
            target.setAnimation(Animation.KNOCKBACK);
            final Location teleTile = getFaceLocation(target, getSize() + 2);
            if (!World.isFloorFree(teleTile, 1)) {
                teleTile.setLocation(getFaceLocation(target, getSize()));
                if (!World.isFloorFree(teleTile, 1)) {
                    return;
                }
            }
            target.lock(1);
            target.setForceMovement(new ForceMovement(teleTile, 30, DirectionUtil.getFaceDirection(getX() - (target.getX() + (target.getSize() / 2.0F)), getY() - (target.getY() + (target.getSize() / 2.0F)))));
            WorldTasksManager.schedule(() -> target.setLocation(teleTile));
        }, delay);
        return delay + 2;
    }

    @Override
    public boolean validate(int id, String name) {
        return id == NpcId.JUSTICIAR_ZACHARIAH_12449;
    }

    @Override
    public int attack(Entity target) {
        if (canUseSpecial(target)) {
            return special(target);
        }
        if (isWithinMeleeDistance(this, target)) {
            setAnimation(getMeleeAnimation());
            var maxHit = CombatUtilities.getRandomMaxHit(this, meleeMax(), meleeAttackType(), target);
            var hit = new Hit(this, maxHit, HitType.MELEE);
            delayHit(this, 0, target, hit);
            return meleeSpeed();
        }
        else {
            setAnimation(getMagicAnimation());
            final CombatSpell spell = validSpell();
            final int delay = 0;
            final int clientDelay = 30;
            final SoundEffect sound = spell.getHitSound();
            final Graphics gfx = spell.getHitGfx();
            final Hit hit = magic(target, 43);
            final boolean splash = hit.getDamage() <=
                ((target instanceof Player player && player.getPrayerManager().isActive(com.zenyte.game.content.skills.prayer.Prayer.PROTECT_FROM_MAGIC) ? 1 : 0));

            World.sendSoundEffect(target.getLocation(), splash ?
                new SoundEffect(227, 10, clientDelay) :
                new SoundEffect(sound.getId(), sound.getRadius(), clientDelay));

            target.setGraphics(splash ?
                new Graphics(85, clientDelay, 124) :
                new Graphics(gfx.getId(), clientDelay, gfx.getHeight()));
            delayHit(delay, target, hit);
            return 6;
        }
    }
}
