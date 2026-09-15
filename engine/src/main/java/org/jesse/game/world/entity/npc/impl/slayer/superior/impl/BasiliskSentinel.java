package org.jesse.game.world.entity.npc.impl.slayer.superior.impl;

import org.jesse.game.content.skills.slayer.SlayerEquipment;
import org.jesse.game.task.WorldTask;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.util.Utils;
import org.jesse.game.world.Projectile;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.masks.Graphics;
import org.jesse.game.world.entity.masks.Hit;
import org.jesse.game.world.entity.masks.HitType;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.npc.combat.CombatScript;
import org.jesse.game.world.entity.npc.impl.slayer.superior.SuperiorNPC;
import org.jesse.game.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

import static org.jesse.game.npc.ids.NpcId.BASILISK_SENTINEL;

public class BasiliskSentinel extends SuperiorNPC implements CombatScript {

    private static final int COOLDOWN_TICKS = 50;
    private int lastSpecial;

    public BasiliskSentinel(@NotNull final Player owner, @NotNull final NPC root, final Location tile) {
        super(owner, root, BASILISK_SENTINEL, tile);
    }

    private static final Projectile PROJECTILE = new Projectile(1735, 72, 100, 0, 55);
    private static final Projectile NO_SHIELD = new Projectile(1739, 72, 100, 0, 55);
    private static final Graphics SPLASH_PROJECTILE = new Graphics(1736, 0, 124);



    @Override
    public int attack(final Entity target) {
        final BasiliskSentinel npc = this;
        if (!(target instanceof Player)) {
            return 0;
        }
        final Player player = (Player) target;

        if(lastSpecial >=  COOLDOWN_TICKS && Utils.random(1) == 0) {
            lastSpecial = 0;
            return specialAttack(player);
        }



        boolean melee = isWithinMeleeDistance(this, player) && Utils.random(1) == 0;
        attackSound();
        if (!SlayerEquipment.MIRROR_SHIELD.isWielding(player)) {
            npc.setAnimation(npc.getCombatDefinitions().getAttackAnim());
            delayHit(npc, 0, player, new Hit(npc, 13, HitType.REGULAR));
            WorldTasksManager.schedule(() -> {
                if (!player.getLocation().withinDistance(target.getLocation(), 15)) {
                    return;
                }
                for (int i = 0; i <= 6; i++) {
                    if (i == 3) {
                        continue;
                    }
                    if (i == 5) {
                        player.getPrayerManager().setPrayerPoints((int) (player.getPrayerManager().getPrayerPoints() * 0.4078947368421053));
                    } else {
                        player.getSkills().setLevel(i, (int) (player.getSkills().getLevel(i) * 0.4078947368421053));
                    }
                }
            }, World.sendProjectile(npc, target, NO_SHIELD));
        } else {
            npc.setAnimation(new Animation(8500));
            if(melee) {
                delayHit(npc, 0, player, new Hit(npc, getRandomMaxHit(npc, npc.getCombatDefinitions().getMaxHit(), MELEE, target), HitType.MELEE));
            } else {
                int delay = World.sendProjectile(this, player, PROJECTILE);
                delayHit(this, delay, player, new Hit(this, getRandomMaxHit(npc, npc.getCombatDefinitions().getMaxHit(), MAGIC, target), HitType.MAGIC).
                        onLand((hit) -> player.setGraphics(SPLASH_PROJECTILE)));
            }
        }
        lastSpecial++;
        return npc.getCombatDefinitions().getAttackSpeed();
    }
    private static final Animation STONE_START = new Animation(8503);
    private static final Animation STONE_MIDDLE = new Animation(8504);
    private static final Animation STONE_END = new Animation(8507);



    private static final Projectile SPECIAL_PROJECTILE = new Projectile(1744, 72, 100, 50, 55);
    private static final Graphics SPECIAL_SPLASH = new Graphics(1738, 0, 124);
    private static final Graphics FREEZE_END_GFX = new Graphics(1743, 0, 0);
    public static final String FROZEN_ATTR = "BREAK_CLICKS";




    public int specialAttack(final Player player) {
        Location aoeLocation = player.getLocation().copy();
        int delay = World.sendProjectile(this, aoeLocation, SPECIAL_PROJECTILE);
        setAnimation(new Animation(8500));

        player.addTemporaryAttribute(FROZEN_ATTR, 1);
        WorldTasksManager.schedule(new WorldTask() {
            private int ticks;
            @Override
            public void run() {
                if(player.getNumericTemporaryAttribute(FROZEN_ATTR).intValue() >= 5) {
                    ticks = 9;//break free
                }
                if(ticks == 1) {
                    if(!player.getLocation().equals(aoeLocation)) {
                        stop();
                        return;
                    }
                    player.sendMessage("You have been entombed in stone.");
                    player.setGraphics(SPECIAL_SPLASH);
                    player.setAnimation(STONE_START);
                    player.freeze(10);
                } else if(ticks > 1 && ticks <= 8) {
                    player.setAnimation(STONE_MIDDLE);
                } else if(ticks == 9) {
                    player.setGraphics(FREEZE_END_GFX);
                    player.setAnimation(STONE_END);
                    player.unlock();
                    player.resetFreeze();
                    stop();
                    return;
                }
                ticks++;
            }
        }, delay, 0);

        return 12;//double it's usual attack time, couldn't find a video to
    }
}
