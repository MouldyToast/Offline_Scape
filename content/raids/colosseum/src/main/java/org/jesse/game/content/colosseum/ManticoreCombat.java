package org.jesse.game.content.colosseum;

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
import org.jesse.game.world.entity.npc.combat.CombatScript;

/**
 * Manticore combat script for the Fortis Colosseum (NPC 12818).
 * <p>
 * 10-tick attack cycle (RSProx-verified, rsprox-1197-rev237):
 * <pre>
 *   +0  triple_charge anim (10868) — no orbs yet
 *   +1  slot 1 orb appears (height 300)
 *   +2  slot 2 orb appears (height 400)
 *   +3  slot 3 orb appears (height 500, always melee)
 *   +4  hold
 *   +5  hold
 *   +6  triple_throw anim (10869), slot 1 cleared, first projectile fires
 *   +7  slot 2 cleared, second projectile fires
 *   +8  slot 3 cleared, third projectile fires
 *   +10 next charge starts
 * </pre>
 * <p>
 * Orb pattern: slots 1+2 are magic/ranged in random order, slot 3 is always melee.
 * <p>
 * Spotanim IDs (same IDs used for both the NPC charge orbs and the fired projectiles):
 * - 2681 = magic,  2683 = ranged,  2685 = melee
 * <p>
 * Impact spotanim IDs (played on the player when an orb hits):
 * - 2682 = magic impact,  2684 = ranged impact,  2686 = melee impact
 * <p>
 * Animation names (from decoded RSProx, correcting earlier binary analysis):
 * - 10866 = death,  10867 = death_explode (Volatility modifier, NOT a combat anim)
 * - 10868 = triple_charge,  10869 = triple_throw
 * - 10870 = spawn_01,  10871 = spawn_02 (no flinch or block anim exists)
 */
public class ManticoreCombat extends ColosseumWaveNpc implements CombatScript {

    private static final Animation CHARGE_ANIM = new Animation(10868);
    private static final Animation THROW_ANIM = new Animation(10869);
    private static final Animation SPAWN_ANIM = new Animation(10871);
    private static final Animation DEATH_ANIM = new Animation(10866);

    // Track the active attack task so a new attack() cancels any stale one.
    private WorldTask activeTask;

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

    // Impact spotanims (played on the player when an orb lands)
    private static final int IMPACT_MAGIC = 2682;
    private static final int IMPACT_RANGED = 2684;
    private static final int IMPACT_MELEE = 2686;

    // Per-slot projectile definitions — each slot fires from a different height on the NPC body.
    // Slot 1 (lowest orb) fires first (delay=0), slot 2 fires slightly after (delay=5).
    // Slot 3 (melee, highest) fires with slot 1.
    // Duration=25 so the orbs arc visibly across the arena (endtime ≈ delay+25 = 25-30).
    private static final Projectile SLOT1_MAGIC_PROJ = new Projectile(PROJ_MAGIC, 200, 124, 0, 0, 25, 0, 0);
    private static final Projectile SLOT1_RANGED_PROJ = new Projectile(PROJ_RANGED, 200, 124, 0, 0, 25, 0, 0);
    private static final Projectile SLOT2_MAGIC_PROJ = new Projectile(PROJ_MAGIC, 260, 124, 5, 0, 25, 0, 0);
    private static final Projectile SLOT2_RANGED_PROJ = new Projectile(PROJ_RANGED, 260, 124, 5, 0, 25, 0, 0);
    private static final Projectile SLOT3_MELEE_PROJ = new Projectile(PROJ_MELEE, 320, 124, 0, 0, 25, 0, 0);

    public ManticoreCombat(int id, Location tile, ColosseumInstance instance) {
        super(id, tile, instance);
    }

    @Override
    public int attack(final Entity target) {
        // Determine orb pattern: first two are magic-ranged or ranged-magic (random), third always melee
        final boolean magicFirst = Utils.random(1) == 0;
        final int slot1Charge = magicFirst ? CHARGE_MAGIC : CHARGE_RANGED;
        final int slot2Charge = magicFirst ? CHARGE_RANGED : CHARGE_MAGIC;

        // If still holding charged orbs waiting for LOS, don't restart the cycle.
        if (activeTask != null) {
            return 1;
        }

        // Tick +0: Charge animation only — no orbs yet.
        setAnimation(CHARGE_ANIM);
        final var avatar = getAvatar();

        // Kill any stale task from a previous attack cycle before starting a new one.
        if (activeTask != null) {
            activeTask.stop();
            activeTask = null;
        }

        // Ticks +1 through +8: orb reveal → hold → throw → fire projectiles.
        // RSProx-verified timing (rsprox-1197-rev237):
        //   +1  slot 1 orb appears
        //   +2  slot 2 orb appears
        //   +3  slot 3 orb (melee) appears
        //   +4  hold (all three visible)
        //   +5  hold
        //   +6  throw anim, clear slot 1, fire projectile 1
        //   +7  clear slot 2, fire projectile 2
        //   +8  clear slot 3, fire projectile 3
        activeTask = new WorldTask() {
            int tick = 0;
            int throwTick = -1;

            @Override
            public void run() {
                if (isDead() || isFinished() || target.isDead() || target.isFinished()) {
                    if (avatar != null) {
                        avatar.getExtendedInfo().setSpotAnim(1, -1, 0, 0);
                        avatar.getExtendedInfo().setSpotAnim(2, -1, 0, 0);
                        avatar.getExtendedInfo().setSpotAnim(3, -1, 0, 0);
                    }
                    activeTask = null;
                    stop();
                    return;
                }

                // === Throw phase (once LOS triggered it) ===
                if (throwTick >= 0) {
                    switch (throwTick) {
                        case 0: { // Throw — clear slot 1, re-assert slots 2+3, fire first projectile
                            setAnimation(THROW_ANIM);
                            if (avatar != null) {
                                avatar.getExtendedInfo().setSpotAnim(1, -1, 0, 0);
                                avatar.getExtendedInfo().setSpotAnim(2, slot2Charge, 0, SLOT_2_HEIGHT);
                                avatar.getExtendedInfo().setSpotAnim(3, CHARGE_MELEE, 0, SLOT_3_HEIGHT);
                            }
                            final Projectile proj1 = magicFirst ? SLOT1_MAGIC_PROJ : SLOT1_RANGED_PROJ;
                            final int delay1 = World.sendProjectile(ManticoreCombat.this, target, proj1);
                            final int clientDelay1 = proj1.getProjectileDuration(ManticoreCombat.this, target);
                            target.setGraphics(new Graphics(magicFirst ? IMPACT_MAGIC : IMPACT_RANGED, clientDelay1, 100));
                            final Hit hit1 = magicFirst
                                    ? magic(target, combatDefinitions.getMaxHit())
                                    : ranged(target, combatDefinitions.getMaxHit());
                            delayHit(delay1, target, hit1);
                            break;
                        }
                        case 1: { // Clear slot 2, re-assert slot 3, fire second projectile
                            if (avatar != null) {
                                avatar.getExtendedInfo().setSpotAnim(2, -1, 0, 0);
                                avatar.getExtendedInfo().setSpotAnim(3, CHARGE_MELEE, 0, SLOT_3_HEIGHT);
                            }
                            final Projectile proj2 = magicFirst ? SLOT2_RANGED_PROJ : SLOT2_MAGIC_PROJ;
                            final int delay2 = World.sendProjectile(ManticoreCombat.this, target, proj2);
                            final int clientDelay2 = proj2.getProjectileDuration(ManticoreCombat.this, target);
                            target.setGraphics(new Graphics(magicFirst ? IMPACT_RANGED : IMPACT_MAGIC, clientDelay2, 100));
                            final Hit hit2 = magicFirst
                                    ? ranged(target, combatDefinitions.getMaxHit())
                                    : magic(target, combatDefinitions.getMaxHit());
                            delayHit(delay2, target, hit2);
                            break;
                        }
                        case 2: { // Clear slot 3 (melee), fire third projectile
                            if (avatar != null) {
                                avatar.getExtendedInfo().setSpotAnim(3, -1, 0, 0);
                            }
                            final int delay3 = World.sendProjectile(ManticoreCombat.this, target, SLOT3_MELEE_PROJ);
                            final int clientDelay3 = SLOT3_MELEE_PROJ.getProjectileDuration(ManticoreCombat.this, target);
                            target.setGraphics(new Graphics(IMPACT_MELEE, clientDelay3, 100));
                            final Hit hit3 = melee(target, combatDefinitions.getMaxHit());
                            delayHit(delay3, target, hit3);
                            activeTask = null;
                            stop();
                            break;
                        }
                    }
                    throwTick++;
                    return;
                }

                // === Charge and hold phase ===
                switch (tick) {
                    case 0: // First orb appears
                        if (avatar != null) {
                            avatar.getExtendedInfo().setSpotAnim(1, slot1Charge, 0, SLOT_1_HEIGHT);
                        }
                        break;
                    case 1: // Second orb appears, re-assert slot 1
                        if (avatar != null) {
                            avatar.getExtendedInfo().setSpotAnim(1, slot1Charge, 0, SLOT_1_HEIGHT);
                            avatar.getExtendedInfo().setSpotAnim(2, slot2Charge, 0, SLOT_2_HEIGHT);
                        }
                        break;
                    default: // Tick 2+: all three orbs charged, hold until LOS
                        // The charge orb seqs (10327/10328/10329) are 20 frames × delay 3
                        // = 60 client cycles = 1200ms = 2 server ticks. Re-asserting every
                        // tick restarts the animation from frame 0 each tick, so it never
                        // plays past the halfway point. Re-assert every 2 ticks instead,
                        // matching the seq duration so each cycle completes before refresh.
                        if (avatar != null && tick % 2 == 0) {
                            avatar.getExtendedInfo().setSpotAnim(1, slot1Charge, 0, SLOT_1_HEIGHT);
                            avatar.getExtendedInfo().setSpotAnim(2, slot2Charge, 0, SLOT_2_HEIGHT);
                            avatar.getExtendedInfo().setSpotAnim(3, CHARGE_MELEE, 0, SLOT_3_HEIGHT);
                        }
                        // Minimum 2-tick hold after all orbs visible, then wait for LOS
                        if (tick >= 4 && !isProjectileClipped(target, false)) {
                            throwTick = 0;
                        }
                        break;
                }
                tick++;
            }
        };
        WorldTasksManager.schedule(activeTask, 1, 0);

        return combatDefinitions.getAttackSpeed();
    }

}