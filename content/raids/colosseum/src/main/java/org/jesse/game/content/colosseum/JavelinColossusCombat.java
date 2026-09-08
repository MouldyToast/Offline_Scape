package org.jesse.game.content.colosseum;

import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.Projectile;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.SoundEffect;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.masks.Graphics;
import org.jesse.game.world.entity.masks.Hit;
import org.jesse.game.world.entity.masks.HitType;
import org.jesse.game.world.entity.npc.combat.CombatScript;
import org.jesse.game.world.entity.player.Player;

/**
 * Javelin Colossus combat script for the Fortis Colosseum (NPC 12817).
 * <p>
 * Dual-mode attack pattern (RSProx-verified across waves 2, 3, 5, 6, 7, 8, 10, 11):
 * <ul>
 *   <li>Standard ranged: anim 10892, projectile 2673 → blockable ranged hit.</li>
 *   <li>Artillery: every 5th attack. Anim 10893, targets player's tile.
 *       3 ticks later, javelin lands with shadow + impact graphics.
 *       Typeless damage if the player is still on the tile. Dodgeable.</li>
 * </ul>
 * <p>
 * Artillery cadence from RSProx: attacks 1-4 = ranged, attack 5 = artillery,
 * attacks 6-9 = ranged, attack 10 = artillery, etc.
 */
public class JavelinColossusCombat extends ColosseumWaveNpc implements CombatScript {

    // Standard ranged attack
    private static final Animation RANGED_ANIM = new Animation(10892);
    private static final Projectile RANGED_PROJ = new Projectile(
            2673,   // graphicsId: npc_colossi_javelin_01_spearhead
            70,    // startHeight (RSProx: 200, not JSON's 70)
            36,    // endHeight   (RSProx: 138, not JSON's 36)
            58,     // delay       (RSProx: starttime=58)
            16,     // angle
            90 - 58,// duration    (RSProx: endtime=90 minus starttime=58 = 32)
            0,      // distanceOffset
            0       // multiplier
    );

    // Artillery attack
    private static final Animation ARTILLERY_ANIM = new Animation(10893);
    private static final Graphics ARTILLERY_FIRE_GFX = new Graphics(2676, 60, 0);
    private static final Graphics ARTILLERY_SHADOW = new Graphics(1446, 30, 0);
    private static final Graphics ARTILLERY_LANDING = new Graphics(2674, 30, 0);
    private static final SoundEffect ARTILLERY_SOUND_1 = new SoundEffect(8356);
    private static final SoundEffect ARTILLERY_SOUND_2 = new SoundEffect(1350, 0, 20);
    private static final int ARTILLERY_LAND_DELAY = 3; // ticks after fire
    private static final int ARTILLERY_MAX_HIT = 48;   // typeless, same as ranged max

    // Every 5th attack is artillery (attacks 5, 10, 15, ...)
    private static final int ARTILLERY_CADENCE = 5;
    private int attackCount = 0;

    public JavelinColossusCombat(int id, Location tile, ColosseumInstance instance) {
        super(id, tile, instance);
    }

    @Override
    public int attack(final Entity target) {
        attackCount++;

        if (attackCount % ARTILLERY_CADENCE == 0) {
            return doArtilleryAttack(target);
        }
        return doRangedAttack(target);
    }

    private int doRangedAttack(final Entity target) {
        setAnimation(RANGED_ANIM);
        final int delay = World.sendProjectile(this, target, RANGED_PROJ);
        delayHit(delay, target, ranged(target, combatDefinitions.getMaxHit()));
        return combatDefinitions.getAttackSpeed();
    }

    private int doArtilleryAttack(final Entity target) {
        // Tick +0: fire animation + spotanim on NPC, snapshot target tile
        setAnimation(ARTILLERY_ANIM);
        setGraphics(ARTILLERY_FIRE_GFX);
        final Location targetTile = new Location(target.getLocation());

        // Tick +3: shadow + landing at the snapshot tile, damage if player is still there
        WorldTasksManager.schedule(() -> {
            if (isDead() || isFinished()) {
                return;
            }
            // Shadow and javelin landing graphics at the target tile
            World.sendGraphics(ARTILLERY_SHADOW, targetTile);
            World.sendGraphics(ARTILLERY_LANDING, targetTile);

            // Sound effects
            World.sendSoundEffect(targetTile, ARTILLERY_SOUND_1);
            World.sendSoundEffect(targetTile, ARTILLERY_SOUND_2);

            // Damage check: if the player is on the target tile, deal typeless damage
            if (target instanceof Player player) {
                if (player.getLocation().matches(targetTile)) {
                    player.applyHit(new Hit(this, ARTILLERY_MAX_HIT, HitType.REGULAR));
                }
            }
        }, ARTILLERY_LAND_DELAY - 1); // 0-indexed: 2 = 3 ticks later

        return combatDefinitions.getAttackSpeed();
    }
}