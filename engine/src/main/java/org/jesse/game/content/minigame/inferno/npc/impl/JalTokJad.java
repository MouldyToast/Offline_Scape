package org.jesse.game.content.minigame.inferno.npc.impl;

import org.jesse.game.content.minigame.inferno.instance.Inferno;
import org.jesse.game.content.minigame.inferno.model.InfernoWave;
import org.jesse.game.content.minigame.inferno.npc.InfernoNPC;
import org.jesse.game.content.minigame.inferno.npc.impl.zuk.AncestralGlyph;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.util.Utils;
import org.jesse.game.util.WorldUtil;
import org.jesse.game.world.Projectile;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.SoundEffect;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.masks.Graphics;
import org.jesse.game.world.entity.masks.Hit;
import org.jesse.game.world.entity.masks.HitType;
import org.jesse.game.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.jesse.game.content.minigame.fightcaves.npcs.TzTokJad.*;

/**
 * @author Tommeh | 29/11/2019 | 19:05
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class JalTokJad extends InfernoNPC {
    private static final Animation meleeAnimation = new Animation(7590);
    private static final Animation rangedAnimation = new Animation(7593);
    private static final Animation magicAnimation = new Animation(7592);
    private static final Graphics rangedGfx = new Graphics(451);
    private static final Graphics magicGfx = new Graphics(157, 0, 96);
    private static final SoundEffect meleeAttackSound = new SoundEffect(408);
    private static final Projectile magicHeadProj = new Projectile(448, 560, 80, 70, 5, 100, 0, 0);
    private static final Projectile magicBodyProj = new Projectile(449, 560, 80, 75, 5, 100, 0, 0);
    private static final Projectile magicTrailProj = new Projectile(450, 560, 80, 80, 5, 100, 0, 0);
    private static final Projectile[] magicProjectiles = new Projectile[] {magicHeadProj, magicBodyProj, magicTrailProj};
    private final int maximumHealth = getMaxHitpoints() >> 1;
    private final List<YtHurKot> healers = new ArrayList<>(5);
    private static final Location[] wave69HealerLocations = {new Location(2270, 5352, 0), new Location(2270, 5353, 0), new Location(2272, 5352, 0)};

    public JalTokJad(final Location location, final Inferno inferno) {
        super(7700, location, inferno);
        setAttackDistance(15);
    }

    @Override
    protected void postHitProcess(Hit hit) {
        if (isDead()) {
            return;
        }
        if (!spawned && getHitpoints() < maximumHealth) {
            spawned = true;
            final int count = inferno.getNPCs(YtHurKot.class).size();
            final int maxCount = inferno.getWave().equals(InfernoWave.WAVE_67) ? 5 : 3;
            for (int index = 0; index < (maxCount - count); index++) {
                final Location location = inferno.getWave().equals(InfernoWave.WAVE_69) ? inferno.getLocation(wave69HealerLocations[index]) : getHealerLocation();
                final YtHurKot healer = new YtHurKot(location, inferno, this);
                healer.spawn();
                healer.faceEntity(this);
                healers.add(healer);
            }
        }
    }

    private Location getHealerLocation() {
        final Optional<Location> optionalLocation = WorldUtil.findEmptySquare(getLocation(), inferno.getWave().equals(InfernoWave.WAVE_67) ? 9 : 6, 1, Optional.of(l -> {
            final int xOffset = Utils.random(1) == 0 ? Utils.random(2) : Utils.random(7, 9);
            final int yOffset = Utils.random(1) == 0 ? Utils.random(2) : Utils.random(7, 9);
            l.setLocation(l.transform(xOffset, yOffset, 0));
            boolean occupied = false;
            for (final YtHurKot healer : healers) {
                if (healer.getLocation().matches(l)) {
                    occupied = true;
                    break;
                }
            }
            final int distance = l.getTileDistance(getLocation());
            return !occupied && distance >= 4;
        }));
        return optionalLocation.orElseGet(() -> getLocation().transform(Utils.random(3), Utils.random(3), 0));
    }

    @Override
    public boolean isFlinchable() {
        return false;
    }

    @Override
    public void heal(final int amount) {
        super.heal(amount);
        if (getHitpoints() >= getMaxHitpoints()) {
            spawned = false;
        }
    }

    @Override
    public int attack(final Entity target) {
        final int style = Utils.random(isWithinMeleeDistance(this, target) ? 2 : 1);
        if (style == 2) {
            playSound(meleeAttackSound);
            setAnimation(meleeAnimation);
            delayHit(0, target, new Hit(this, getRandomMaxHit(this, 97, STAB, target), HitType.MELEE));
        }
        else if (style == 1) {
            setAnimation(rangedAnimation);
            playSound(meleeAttackSound);
            WorldTasksManager.schedule(() -> {
                if (targetInFightCaves(target)) {
                    target.setGraphics(RANGED_GFX);

                    delayHit(1, target, new Hit(this, getRandomMaxHit(this, 97, RANGED, target), HitType.RANGED).onLand(
                            (hit -> {
                                playSound(mageLandSound);
                                target.setGraphics(MAGIC_GFX);
                            })
                    ));
                }
            }, 2);

        }
        else {
            playSound(new SoundEffect(mageAttackSound.getId(), mageAttackSound.getRadius(), 20));
            setAnimation(magicAnimation);
            final int tickTime = MAGIC_PROJECTILES[0].getTime(this, target);
            final int clientTime = MAGIC_PROJECTILES[0].getProjectileDuration(this, target);
            for (final Projectile projectile : MAGIC_PROJECTILES) {
                World.sendProjectile(this, target, projectile);
            }
            target.setGraphics(new Graphics(MAGIC_GFX.getId(), clientTime, 96));
            playSound(new SoundEffect(mageLandSound.getId(), mageLandSound.getRadius(), clientTime));
            WorldTasksManager.schedule(() -> {
                if (targetInFightCaves(target))
                    delayHit(1, target, new Hit(this, getRandomMaxHit(this, 97, MAGIC, target), HitType.MAGIC));
            }, Math.max(tickTime - 1, 0));
        }
        return combatDefinitions.getAttackSpeed();
    }

    protected void playSound(@NotNull final SoundEffect sound) {
        inferno.playSound(sound);
    }

    private static boolean targetInFightCaves(Entity target) {
        return target instanceof AncestralGlyph || target instanceof Player playerTarget && playerTarget.getArea() instanceof Inferno;
    }

    @Override
    protected void onDeath(Entity source) {
        super.onDeath(source);
        for (final YtHurKot healer : healers) {
            healer.sendDeath();
        }
        healers.clear();
    }
}
