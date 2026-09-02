package com.zenyte.game.content.colosseum;

import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPCCombat;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.npc.combat.CombatScript;

/**
 * Fremennik Warband combat script for the Fortis Colosseum.
 * One class handles all three NPC types (Archer 12814, Seer 12815, Berserker 12816).
 * <p>
 * <b>Weak-to-style</b> (OSRS Wiki): attacking a warbander with the style they are
 * weakest to always results in a successful hit at the player's maximum damage.
 * <ul>
 *   <li>Berserker (melee) — weak to magic</li>
 *   <li>Seer (magic) — weak to ranged</li>
 *   <li>Archer (ranged) — weak to melee</li>
 * </ul>
 * Implemented via {@link #isAlwaysTakeMaxHit(HitType)}, which the player combat
 * system checks before rolling accuracy (MeleeCombat:182, RangedCombat:302,
 * MagicCombat:129).
 * <p>
 * <b>Attack stagger</b> (OSRS Wiki, RSProx-verified waves 3–6): all Fremennik
 * warbanders share a global 6-tick cycle anchored at the interface confirm press.
 * Berserker attacks on cycle slot 2, seer on slot 3, archer on slot 4. Applied
 * via combatDelay offsets in {@link ColosseumInstance#startWave()}: berserker +0,
 * seer +1, archer +2 relative to SPAWN_ATTACK_DELAY_TICKS. The cycle is
 * enforced by {@link #afterMovement()} — skipped or failed attacks always
 * reset to a full 6-tick wait, preventing drift.
 * <p>
 * <b>Melee range</b>: all three have attackDistance=1 in JSON. Archer and Seer
 * fire short-range projectiles at melee distance (RSProx: starttime=10,
 * endtime=30 — about 1 tile travel). Visual only; the hit type comes from JSON.
 * <p>
 * <b>Routefinding</b> (OSRS Wiki): warbanders use routefinding to navigate
 * around arena pillars to reach the player. Enabled via {@link #isIntelligent()}.
 * <p>
 * <b>Movement speed</b> (RSProx-verified waves 1–6): warbanders run (2 tiles/tick)
 * when approaching the player, walking only for the final tile. Set via
 * {@code setRun(true)} in the constructor.
 * <p>
 * <b>Fixed cycle / skip on movement</b> (OSRS Wiki): warbanders only attack
 * when in melee distance AND not moving. If a warbander is still walking toward
 * the player when its attack tick arrives, it skips that attack and waits for
 * the next 6-tick cycle. Implemented via {@link #afterMovement()} override:
 * if the NPC moved this tick ({@code getWalkDirection() != -1}), the attack is
 * skipped and combatDelay is reset to attackSpeed.
 */
public class FremennikWarbandCombat extends ColosseumWaveNpc implements CombatScript {

    // Per-NPC attack data (set in constructor)
    private final Animation attackAnim;
    private final Projectile projectile; // null for Berserker (melee, no projectile)
    private final HitType hitType;
    private final HitType weakToHitType;

    // Berserker — melee, weak to magic
    private static final Animation BERSERKER_ANIM = new Animation(10856);

    // Archer — ranged at melee range, weak to melee
    private static final Animation ARCHER_ANIM = new Animation(10850);
    // Projectile constructor: graphicsId, startHeight, endHeight, delay, angle, duration, distanceOffset, multiplier
    private static final Projectile ARCHER_PROJ = new Projectile(
            9,    // iron_arrow_travel
            163,  // startHeight (RSProx)
            146,  // endHeight (RSProx)
            10,   // delay (RSProx: starttime=10)
            15,   // angle (RSProx: angle=15)
            20,   // duration (RSProx: endtime=30 minus starttime=10)
            64,   // distanceOffset (RSProx: progress=64)
            5     // multiplier
    );

    // Seer — magic at melee range, weak to ranged
    private static final Animation SEER_ANIM = new Animation(10853);
    private static final Projectile SEER_PROJ = new Projectile(
            130,  // fireblast_travel
            172,  // startHeight (RSProx)
            124,  // endHeight (RSProx)
            10,   // delay (RSProx: starttime=10)
            16,   // angle (RSProx: angle=16)
            20,   // duration (RSProx: endtime=30 minus starttime=10)
            64,   // distanceOffset (RSProx: progress=64)
            5     // multiplier
    );

    public FremennikWarbandCombat(int id, Location tile, ColosseumInstance instance) {
        super(id, tile, instance);
        setRun(true);
        setForceFollowClose(true);
        switch (id) {
            case NpcId.FREMENNIK_WARBAND_BERSERKER -> {
                attackAnim = BERSERKER_ANIM;
                projectile = null;
                hitType = HitType.MELEE;
                weakToHitType = HitType.MAGIC;
            }
            case NpcId.FREMENNIK_WARBAND_ARCHER -> {
                attackAnim = ARCHER_ANIM;
                projectile = ARCHER_PROJ;
                hitType = HitType.RANGED;
                weakToHitType = HitType.MELEE;
            }
            case NpcId.FREMENNIK_WARBAND_SEER -> {
                attackAnim = SEER_ANIM;
                projectile = SEER_PROJ;
                hitType = HitType.MAGIC;
                weakToHitType = HitType.RANGED;
            }
            default -> throw new IllegalArgumentException("Unknown Fremennik warband NPC: " + id);
        }
    }

    @Override
    public int attack(final Entity target) {
        setAnimation(attackAnim);
        final int maxHit = combatDefinitions.getMaxHit();
        if (projectile != null) {
            final int delay = World.sendProjectile(this, target, projectile);
            delayHit(delay, target, hitType == HitType.RANGED
                    ? ranged(target, maxHit)
                    : magic(target, maxHit));
        } else {
            delayHit(0, target, melee(target, maxHit));
        }
        return combatDefinitions.getAttackSpeed();
    }

    /**
     * Weak-to-style mechanic. The player combat system checks this via
     * {@code CombatUtilities.isAlwaysTakeMaxHit(target, hitType)} before rolling
     * accuracy (MeleeCombat:182, RangedCombat:302, MagicCombat:129). Returning
     * {@code true} causes the player's {@code getRandomHit()} to skip the accuracy
     * roll and return the player's calculated max hit directly.
     *
     * @param type the HitType of the incoming player attack
     * @return true if the player is using the style this NPC is weak to
     */
    @Override
    public boolean isAlwaysTakeMaxHit(HitType type) {
        return type == weakToHitType;
    }

    /**
     * Enable routefinding so warbanders navigate around arena pillars.
     * Without this, they use basic straight-line pathing and get stuck.
     */
    @Override
    public boolean isIntelligent() {
        return true;
    }

    /**
     * Global 6-tick wave cycle (OSRS Wiki, RSProx-verified waves 3–6).
     * <p>
     * All Fremennik warbanders share a single 6-tick cycle anchored at the
     * interface confirm press. Berserker attacks on cycle slot 2, seer on 3,
     * archer on 4 — applied via initial combatDelay offsets in startWave().
     * <p>
     * This override enforces two rules that keep NPCs locked to the cycle:
     * <ol>
     *   <li>If the NPC moved this tick (still walking), skip the attack.</li>
     *   <li>Whether the attack succeeded, failed (out of range / clipped), or
     *       was skipped, always reset combatDelay to a full attackSpeed (6).
     *       This prevents the standard combat system's retry-every-tick
     *       behaviour from drifting the NPC off its cycle slot.</li>
     * </ol>
     */
    @Override
    public void afterMovement() {
        NPCCombat c = getCombat();
        if (c.getCombatDelay() <= 0) {
            // Our slot in the global 6-tick cycle arrived
            if (getWalkDirection() == -1) {
                // Stationary — let the standard combat system attempt the attack
                c.processAttack();
            }
            // Always re-sync to the cycle. If attack succeeded, combatDelay
            // is already 6. If it failed (returned 0) or we were moving, force 6.
            if (c.getCombatDelay() < combatDefinitions.getAttackSpeed()) {
                c.setCombatDelay(combatDefinitions.getAttackSpeed());
            }
        }
        // Do NOT call super — we fully own the attack timing
    }
}