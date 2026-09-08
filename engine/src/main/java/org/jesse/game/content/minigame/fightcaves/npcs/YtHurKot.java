package org.jesse.game.content.minigame.fightcaves.npcs;

import org.jesse.game.content.minigame.fightcaves.FightCaves;
import org.jesse.game.util.Utils;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.SoundEffect;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.masks.Graphics;
import org.jesse.game.world.entity.npc.combat.CombatScript;
import org.jesse.game.world.entity.player.action.combat.CombatUtilities;

import java.util.Optional;

/**
 * @author Kris | 27/10/2018 18:51
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 * <p>
 * <p>
 * YtHur-Kots will heal the TxTok-Jad if they're at least within 4 mapsquares of it.
 * <p>
 * If the YtHur-Kot is under combat, it will only attempt to heal jad if it is within
 * melee distance of its current target, otherwise if the target is too far away,
 * even if the YtHur-Kot is right next to TzTok-Jad, it will not heal it.
 * <p>
 * YtHur-Kot will heal TzTok-Jad every four game ticks for 1-10 health, granted
 * the above predicate is true.
 * <p>
 * YtHur-Kot will by default follow TzTok-Jad, unless attacked by the player.
 */
final class YtHurKot extends FightCavesNPC implements CombatScript {
    private static final SoundEffect attackSound = new SoundEffect(608);
    private static final Graphics GFX = new Graphics(444, 0, 550);
    private static final Animation ANIMATION = new Animation(2639);
    private static final SoundEffect healSound = new SoundEffect(167);
    private final TzTokJad jad;
    private int ticks;

    YtHurKot(final TzHaarNPC npc, final Location tile, final FightCaves caves) {
        super(npc, tile, caves);
        final Optional<TzTokJad> optional = caves.getJad();
        jad = optional.orElse(null);
    }

    @Override
    public boolean isForceAggressive() {
        return false;
    }

    @Override
    public void processNPC() {
        final boolean process = combat.process();
        if (!(process || jad == null || isDead())) {
            if (!hasWalkSteps()) {
                if (!CombatUtilities.isWithinMeleeDistance(jad, this)) {
                    calcFollow(jad, 64, true, true, false);
                    setFaceEntity(jad);
                }
            }
        }
        if (++ticks % 4 != 0 || process || jad == null || !CombatUtilities.isWithinMeleeDistance(jad, this)) {
            return;
        }
        playSound(healSound);
        setAnimation(ANIMATION);
        jad.heal(Utils.random(1, 5));
        World.sendGraphics(GFX, jad.getMiddleLocation());
    }

    @Override
    public int attack(Entity target) {
        animate();
        playSound(attackSound);
        delayHit(0, target, melee(target, combatDefinitions.getMaxHit()));
        return combatDefinitions.getAttackSpeed();
    }
}
