package com.zenyte.game.content.colosseum;

import com.zenyte.game.task.WorldTask;
import com.zenyte.game.task.WorldTasksManager;
import com.zenyte.game.util.Direction;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.Hit;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.Spawnable;
import com.zenyte.game.world.entity.npc.combat.CombatScript;

/**
 * Manticore combat script for the Fortis Colosseum (NPC 12818).
 * <p>
 * Attack sequence (10-tick cycle):
 * 1. Charge phase: plays seq 10868 + shows 3 charge spotanims in slots 1/2/3
 * 2. Throw phase (1 tick later): plays seq 10869 + fires 3 projectiles
 * 3. Each orb hits independently with its own attack style
 * <p>
 * Orb pattern (RSProx-verified):
 * - Slot 1 + Slot 2: magic-ranged or ranged-magic (random)
 * - Slot 3: ALWAYS melee
 * <p>
 * Charge spotanim IDs (shown on NPC during wind-up):
 * - 2681 = magic charge (height 300 at slot 1, 400 at slot 2)
 * - 2683 = ranged charge (height 300 at slot 1, 400 at slot 2)
 * - 2685 = melee charge (always height 500 at slot 3)
 * <p>
 * Projectile spotanim IDs (fired at player):
 * - 2663 = magic projectile
 * - 2662 = ranged projectile
 * - 2664 = melee projectile
 */
public class ManticoreCombat extends NPC implements CombatScript, Spawnable {

    private static final Animation CHARGE_ANIM = new Animation(10868);
    private static final Animation THROW_ANIM = new Animation(10869);
    private static final Animation SPAWN_ANIM = new Animation(10871);
    private static final Animation DEATH_ANIM = new Animation(10866);

    // Charge spotanims (shown on NPC body during wind-up)
    private static final int CHARGE_MAGIC = 2681;
    private static final int CHARGE_RANGED = 2683;
    private static final int CHARGE_MELEE = 2685;

    // Charge heights per slot
    private static final int SLOT_1_HEIGHT = 300;
    private static final int SLOT_2_HEIGHT = 400;
    private static final int SLOT_3_HEIGHT = 500;

    // Projectile spotanims (fired at player) — same graphic as the charge orbs
    private static final int PROJ_MAGIC = 2681;
    private static final int PROJ_RANGED = 2683;
    private static final int PROJ_MELEE = 2685;

    // Per-slot projectile definitions — each slot fires from a different height on the NPC body.
    // Slot 1 (lowest orb) fires first (delay=0), slot 2 fires slightly after (delay=5).
    // Slot 3 (melee, highest) fires with slot 1.
    // Duration=25 so the orbs arc visibly across the arena (endtime ≈ delay+25 = 25-30).
    private static final Projectile SLOT1_MAGIC_PROJ = new Projectile(PROJ_MAGIC, 50, 31, 0, 0, 25, 0, 0);
    private static final Projectile SLOT1_RANGED_PROJ = new Projectile(PROJ_RANGED, 50, 31, 0, 0, 25, 0, 0);
    private static final Projectile SLOT2_MAGIC_PROJ = new Projectile(PROJ_MAGIC, 65, 31, 5, 0, 25, 0, 0);
    private static final Projectile SLOT2_RANGED_PROJ = new Projectile(PROJ_RANGED, 65, 31, 5, 0, 25, 0, 0);
    private static final Projectile SLOT3_MELEE_PROJ = new Projectile(PROJ_MELEE, 80, 31, 0, 0, 25, 0, 0);

    public ManticoreCombat(int id, Location tile, Direction facing, int radius) {
        super(id, tile, facing, radius);
    }

    @Override
    public int attack(final Entity target) {
        // Determine orb pattern: first two are magic-ranged or ranged-magic (random), third always melee
        final boolean magicFirst = Utils.random(1) == 0;
        final int slot1Charge = magicFirst ? CHARGE_MAGIC : CHARGE_RANGED;
        final int slot2Charge = magicFirst ? CHARGE_RANGED : CHARGE_MAGIC;

        // Phase 1: Charge — show wind-up animation + 3 charge spotanims on NPC
        setAnimation(CHARGE_ANIM);
        final var avatar = getAvatar();
        if (avatar != null) {
            avatar.getExtendedInfo().setSpotAnim(1, slot1Charge, 0, SLOT_1_HEIGHT);
            avatar.getExtendedInfo().setSpotAnim(2, slot2Charge, 0, SLOT_2_HEIGHT);
            avatar.getExtendedInfo().setSpotAnim(3, CHARGE_MELEE, 0, SLOT_3_HEIGHT);
        }

        // Phase 2: Throw — fire one projectile per tick, 3 ticks total
        WorldTasksManager.schedule(new WorldTask() {
            int tick = 0;

            @Override
            public void run() {
                if (isDead() || isFinished() || target.isDead() || target.isFinished()) {
                    stop();
                    return;
                }

                if (tick == 0) {
                    setAnimation(THROW_ANIM);

                    // Fire slot 1 — clear it, but re-assert slots 2+3 so they stay visible
                    if (avatar != null) {
                        avatar.getExtendedInfo().setSpotAnim(1, -1, 0, 0);
                        avatar.getExtendedInfo().setSpotAnim(2, slot2Charge, 0, SLOT_2_HEIGHT);
                        avatar.getExtendedInfo().setSpotAnim(3, CHARGE_MELEE, 0, SLOT_3_HEIGHT);
                    }
                    final Projectile proj1 = magicFirst ? SLOT1_MAGIC_PROJ : SLOT1_RANGED_PROJ;
                    final int delay1 = World.sendProjectile(ManticoreCombat.this, target, proj1);
                    final Hit hit1 = magicFirst
                            ? magic(target, combatDefinitions.getMaxHit())
                            : ranged(target, combatDefinitions.getMaxHit());
                    delayHit(delay1, target, hit1);
                } else if (tick == 1) {
                    // Fire slot 2 — clear it, re-assert slot 3
                    if (avatar != null) {
                        avatar.getExtendedInfo().setSpotAnim(2, -1, 0, 0);
                        avatar.getExtendedInfo().setSpotAnim(3, CHARGE_MELEE, 0, SLOT_3_HEIGHT);
                    }
                    final Projectile proj2 = magicFirst ? SLOT2_RANGED_PROJ : SLOT2_MAGIC_PROJ;
                    final int delay2 = World.sendProjectile(ManticoreCombat.this, target, proj2);
                    final Hit hit2 = magicFirst
                            ? ranged(target, combatDefinitions.getMaxHit())
                            : magic(target, combatDefinitions.getMaxHit());
                    delayHit(delay2, target, hit2);
                } else if (tick == 2) {
                    // Fire slot 3 — clear it, all gone
                    if (avatar != null) {
                        avatar.getExtendedInfo().setSpotAnim(3, -1, 0, 0);
                    }
                    final int delay3 = World.sendProjectile(ManticoreCombat.this, target, SLOT3_MELEE_PROJ);
                    final Hit hit3 = melee(target, combatDefinitions.getMaxHit());
                    delayHit(delay3, target, hit3);
                    stop();
                }
                tick++;
            }
        }, 1, 0);

        return combatDefinitions.getAttackSpeed();
    }

    @Override
    public boolean validate(int id, String name) {
        return id == 12818;
    }
}