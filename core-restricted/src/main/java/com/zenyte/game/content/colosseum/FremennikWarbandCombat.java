package com.zenyte.game.content.colosseum;

import com.zenyte.game.world.Projectile;
import com.zenyte.game.world.World;
import com.zenyte.game.world.entity.Entity;
import com.zenyte.game.world.entity.Location;
import com.zenyte.game.world.entity.masks.Animation;
import com.zenyte.game.world.entity.masks.HitType;
import com.zenyte.game.world.entity.npc.NPCCombat;
import com.zenyte.game.world.entity.npc.NpcId;
import com.zenyte.game.world.entity.pathfinding.RouteFinder;
import com.zenyte.game.world.entity.pathfinding.RouteResult;
import com.zenyte.game.world.entity.pathfinding.strategy.EntityStrategy;
import com.zenyte.game.world.entity.pathfinding.strategy.TileStrategy;
import com.zenyte.game.world.Position;
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

    /**
     * Preferred standing tile relative to the player (RSProx-verified all 11 waves):
     * Berserker (0,+1) north, Archer (-1,0) west, Seer (+1,0) east.
     * Quartet extra archer overridden to (0,-1) south via {@link #setPreferredOffset}.
     */
    private int preferredDx;
    private int preferredDy;

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
                preferredDx = 0; preferredDy = 1;   // north
            }
            case NpcId.FREMENNIK_WARBAND_ARCHER -> {
                attackAnim = ARCHER_ANIM;
                projectile = ARCHER_PROJ;
                hitType = HitType.RANGED;
                weakToHitType = HitType.MELEE;
                preferredDx = -1; preferredDy = 0;  // west (Quartet extra: overridden to south)
            }
            case NpcId.FREMENNIK_WARBAND_SEER -> {
                attackAnim = SEER_ANIM;
                projectile = SEER_PROJ;
                hitType = HitType.MAGIC;
                weakToHitType = HitType.RANGED;
                preferredDx = 1; preferredDy = 0;   // east
            }
            default -> throw new IllegalArgumentException("Unknown Fremennik warband NPC: " + id);
        }
        // NPCCombat override: force melee-style diagonal rejection and preferred-tile movement.
        // isMelee(): two code paths in NPCCombat check diagonals separately —
        //   combatAttack() line 60 uses virtual isMelee(), appendMovement() line 328 reads
        //   JSON directly. Overriding both here handles both paths.
        // appendMovement(): re-paths whenever the NPC is not on its preferred cardinal tile
        //   relative to the player, not just when out of generic melee range.
        this.combat = new NPCCombat(this) {
            @Override
            public boolean isMelee() {
                return true;
            }

            @Override
            protected boolean appendMovement() {
                final boolean onPreferred = npc.getX() == target.getX() + preferredDx
                        && npc.getY() == target.getY() + preferredDy;
                if (!onPreferred || npc.isProjectileClipped(target, true)) {
                    npc.resetWalkSteps();
                    npc.calcFollow(target, npc.isRun() ? 2 : 1, true, npc.isIntelligent(), npc.isEntityClipped());
                }
                return true;
            }
        };
    }

    /**
     * Override preferred standing direction for this NPC. Used by
     * {@link ColosseumInstance#startWave()} to assign the Quartet extra archer
     * to south (0, -1) instead of the default west (-1, 0).
     */
    void setPreferredOffset(int dx, int dy) {
        this.preferredDx = dx;
        this.preferredDy = dy;
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
     * Pathfind to the preferred cardinal tile relative to the player
     * (RSProx-verified: Berserker→north, Seer→east, Archer→west, 100% of attacks).
     * <p>
     * Uses {@link RouteFinder#findConditionalRoute} which includes
     * {@code OCCUPIED_BLOCK_NPC} in collision checks, so warbanders route
     * around each other rather than stacking single-file.
     * <p>
     * If the preferred tile is unreachable (e.g. pillar between player and
     * that side), falls back to {@link EntityStrategy} for any adjacent tile.
     */
    @Override
    public boolean calcFollow(Position target, int maxStepsCount, boolean calculate,
                              boolean intelligent, boolean checkEntities) {
        if (intelligent && target instanceof Entity entity) {
            final int prefX = entity.getX() + preferredDx;
            final int prefY = entity.getY() + preferredDy;
            // Already on the preferred tile
            if (getX() == prefX && getY() == prefY) {
                return true;
            }
            // Try preferred tile first
            RouteResult steps = RouteFinder.findConditionalRoute(
                    this, getSize(), new TileStrategy(prefX, prefY), true);
            if (steps != RouteResult.ILLEGAL && steps.getSteps() > 0) {
                return applyRoute(steps, maxStepsCount);
            }
            // Preferred tile unreachable — fall back to any adjacent tile
            return applyRoute(
                    RouteFinder.findConditionalRoute(this, getSize(), new EntityStrategy(entity), true),
                    maxStepsCount);
        }
        return super.calcFollow(target, maxStepsCount, calculate, intelligent, checkEntities);
    }

    /** Walk along a computed route, returning false only if the route is illegal. */
    private boolean applyRoute(RouteResult steps, int maxStepsCount) {
        if (steps == RouteResult.ILLEGAL) {
            return false;
        }
        if (steps.getSteps() == 0) {
            return true;
        }
        final int[] bufferX = steps.getXBuffer();
        final int[] bufferY = steps.getYBuffer();
        int stepCount = 0;
        for (int step = steps.getSteps() - 1; step >= 0; step--) {
            if (!addWalkStepsInteract(bufferX[step], bufferY[step], maxStepsCount, getSize(), true)) {
                break;
            }
            if (maxStepsCount != -1 && ++stepCount >= maxStepsCount) {
                break;
            }
        }
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