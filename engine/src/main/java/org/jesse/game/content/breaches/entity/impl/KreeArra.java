package org.jesse.game.content.breaches.entity.impl;

import org.jesse.game.content.breaches.entity.BreachEntity;
import org.jesse.game.util.Direction;
import org.jesse.game.util.DirectionUtil;
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
import org.jesse.game.npc.ids.NpcId;
import org.jesse.game.world.entity.npc.Spawnable;
import org.jesse.game.world.entity.npc.combat.CombatScript;
import org.jesse.game.world.entity.player.MovementLock;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.action.combat.PlayerCombat;
import org.jesse.utils.TimeUnit;
import org.jetbrains.annotations.NotNull;

public class KreeArra extends BreachEntity implements Spawnable, CombatScript {
    long clickDelay;

    public KreeArra(int id, Location tile, Direction facing, int radius) {
        super(id, tile, facing, radius);
    }

    @Override
    public void processNPC() {
        super.processNPC();
        if (isForceFollowClose()) {
            if (clickDelay > Utils.currentTimeMillis()) {
                setForceFollowClose(false);
            }
        } else {
            if (clickDelay < Utils.currentTimeMillis()) {
                setForceFollowClose(true);
            }
        }
    }

    @Override
    public boolean validate(final int id, final String name) {
        return id == NpcId.KREEARRA_12443;
    }

    private static final Animation meleeAnimation = new Animation(6981);
    private static final Animation distancedAnimation = new Animation(6980);
    private static final Projectile magicProjectile = new Projectile(1200, 164, 64, 40, 5, 10, 0, 5);
    private static final Projectile rangedProjectile = new Projectile(1199, 164, 64, 40, 5, 10, 0, 5);
    private static final SoundEffect meleeSound = new SoundEffect(3892, 10, 0);
    public static final SoundEffect TORNADO_SOUND = new SoundEffect(3870, 10, 0);
    public static final SoundEffect TORNADO_HIT_SOUND = new SoundEffect(2727, 10, -1);
    public static final SoundEffect TORNADO_SPLASH_SOUND = new SoundEffect(227, 10, -1);

    @Override
    public int attack(final Entity target) {
        final KreeArra npc = this;
        if (npc.isForceFollowClose() && Utils.random(1) == 0) {
            final int distanceX = target.getX() - npc.getX();
            final int distanceY = target.getY() - npc.getY();
            final int size = npc.getSize();
            if (distanceX > size || distanceX < -1 || distanceY > size || distanceY < -1) {
                return 0;
            }
            npc.setAnimation(meleeAnimation);
            World.sendSoundEffect(getMiddleLocation(), meleeSound);
            delayHit(npc, 0, target, new Hit(npc, getRandomMaxHit(npc, 26, MELEE, MAGIC, target), HitType.MELEE));
            return npc.getCombatDefinitions().getAttackSpeed();
        }
        npc.setAnimation(distancedAnimation);
        World.sendSoundEffect(getMiddleLocation(), TORNADO_SOUND);
        for (final Entity t : npc.getPossibleTargets(EntityType.PLAYER)) {
            if (t instanceof Player targetPlayer) {
                final int style = Utils.random(1);

                if (style == 0) {
                    int damage = getRandomMaxHit(npc, 21, MAGIC, RANGED, t);
                    //kree'arra deals a minimum of 10 damage upon successful hit; for even distribution, we re-calc it.
                    if (damage > 0)
                        damage = Utils.random(10, 21);
                    final Hit hit = new Hit(npc, damage, HitType.MAGIC);
                    fireTornadoPush(targetPlayer, hit, magicProjectile);
                }
                else {
                    final Hit hit = new Hit(npc, getRandomMaxHit(npc, 71, RANGED, t), HitType.RANGED);
                    fireTornadoPush(targetPlayer, hit, rangedProjectile);
                }

            }
        }
        return npc.getCombatDefinitions().getAttackSpeed();
    }

    private void fireTornadoPush(Player target, Hit hit, Projectile projectile) {
        if (target.isDead() || target.isFinished()) return;
        var delay = World.sendProjectile(this, target, projectile);
        delayHit(this, delay, target, hit);
        var sound = (hit.getDamage() == 0 ? TORNADO_SPLASH_SOUND : TORNADO_HIT_SOUND);
        World.sendSoundEffect(target.getLocation(), sound.withDelay(projectile.getProjectileDuration(getMiddleLocation(), target)));
        if (Utils.random(2) == 0)
            push(target);
    }

    @Override
    public void handleIngoingHit(Hit hit) {
        super.handleIngoingHit(hit);
    }

    private static final Animation knockbackAnimation = new Animation(848);
    private static final Graphics stunGraphics = new Graphics(348, 0, 92);

    private void push(@NotNull final Player player) {
        if (player.isDead() || player.isFinished()) return;
        final Location tile = player.getFaceLocation(this, 2, 1024);
        final Location destination = new Location(player.getLocation());
        final int dir = DirectionUtil.getMoveDirection(tile.getX() - destination.getX(), tile.getY() - destination.getY());
        if (dir != -1) {
            if (World.checkWalkStep(destination, dir, player.getSize(), false, false))
                destination.setLocation(tile);
        }
        player.faceEntity(this);
        if (!destination.matches(player))
            player.setLocation(destination);

        //50% chance to stun regardless if teleported or not.
        if (Utils.random(1) == 0) {
            if (player.getActionManager().getAction() instanceof PlayerCombat && player.getActionManager().getActionDelay() == 0)
                player.getActionManager().addActionDelay(1);

            var lockTime = System.currentTimeMillis() + TimeUnit.TICKS.toMillis(1);
            var movementLock = new MovementLock(lockTime, "You're stunned.");
            player.addMovementLock(movementLock);
            player.setAnimation(knockbackAnimation);
            player.setGraphics(stunGraphics);
        }
    }
}
