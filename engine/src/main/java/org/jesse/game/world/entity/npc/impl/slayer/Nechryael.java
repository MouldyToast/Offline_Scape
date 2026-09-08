package org.jesse.game.world.entity.npc.impl.slayer;

import org.jesse.game.util.Direction;
import org.jesse.game.util.Utils;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.SoundEffect;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.masks.Graphics;
import org.jesse.game.world.entity.masks.Hit;
import org.jesse.game.world.entity.masks.HitType;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.npc.Spawnable;
import org.jesse.game.world.entity.npc.combat.CombatScript;

/**
 * @author Tommeh | 13 dec. 2017 : 19:29:28
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class Nechryael extends NPC implements Spawnable, CombatScript {
    private static final Graphics TELEPORT_GRAPHICS = new Graphics(333);
    private static final Animation SPECIAL_ATTACK_ANIMATION = new Animation(1529);
    private static final byte[][] BASIC_OFFSETS = new byte[][] {{0, -1}, {-1, 0}, {0, 1}, {1, 0}};
    private int spawnCount;

    public Nechryael(final int id, final Location tile, final Direction direction, final int radius) {
        super(id, tile, direction, radius);
        spawnCount = 0;
    }

    @Override
    public void onFinish(final Entity source) {
        super.onFinish(source);
        this.spawnCount = 0;
    }

    private static final SoundEffect deathSpawnSound = new SoundEffect(2418);

    @Override
    public int attack(Entity target) {
        setAnimation(getCombatDefinitions().getAttackAnim());
        attackSound();
        delayHit(this, 0, target, new Hit(this, getRandomMaxHit(this, getCombatDefinitions().getMaxHit(), MELEE, target), HitType.MELEE));
        if (Utils.random(5) == 0) {
            Location tile;
            int tryCount = 0;
            while (true) {
                if (tryCount++ == 100) {
                    return getCombatDefinitions().getAttackSpeed();
                }
                final byte[] offsets = BASIC_OFFSETS[Utils.random(BASIC_OFFSETS.length - 1)];
                if (World.isTileFree(tile = new Location(getX() + offsets[0], getY() + offsets[1], getPlane()), 1)) {
                    break;
                }
            }
            if (spawnCount <= 1) {
                final DeathSpawn deathSpawn = new DeathSpawn(10, tile, Direction.SOUTH, 5);
                deathSpawn.spawn();
                World.sendSoundEffect(tile, deathSpawnSound);
                setAnimation(SPECIAL_ATTACK_ANIMATION);
                World.sendGraphics(TELEPORT_GRAPHICS, tile);
                deathSpawn.getCombat().setTarget(target); //TODO this doesnt work if the area isnt multi
                spawnCount++;
            }
        }
        return getCombatDefinitions().getAttackSpeed();
    }

    @Override
    public boolean validate(final int id, final String name) {
        return id == 8 || id == 7278;
    }

    public int getSpawnCount() {
        return spawnCount;
    }

    public void setSpawnCount(int spawnCount) {
        this.spawnCount = spawnCount;
    }
}
