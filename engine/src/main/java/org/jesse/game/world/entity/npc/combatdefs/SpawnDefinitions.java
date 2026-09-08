package org.jesse.game.world.entity.npc.combatdefs;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import org.jesse.game.world.entity.SoundEffect;
import org.jesse.game.world.entity.masks.Animation;

/**
 * @author Kris | 18/11/2018 02:54
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class SpawnDefinitions {
    private int respawnDelay = 25;
    private Animation deathAnimation = Animation.STOP;
    private Animation spawnAnimation = Animation.STOP;
    private SoundEffect deathSound;
    private SoundEffect spawnSound;

    public static SpawnDefinitions construct(final SpawnDefinitions clone) {
        final SpawnDefinitions defs = new SpawnDefinitions();
        if (clone == null) return defs;
        defs.respawnDelay = clone.respawnDelay;
        defs.deathAnimation = clone.deathAnimation;
        defs.spawnAnimation = clone.spawnAnimation;
        defs.deathSound = clone.deathSound;
        defs.spawnSound = clone.spawnSound;
        return defs;
    }

    public int getRespawnDelay() {
        return respawnDelay;
    }

    public void setRespawnDelay(int respawnDelay) {
        this.respawnDelay = respawnDelay;
    }

    public Animation getDeathAnimation() {
        return deathAnimation;
    }

    @JsonSetter(nulls = Nulls.SKIP)
    public void setDeathAnimation(Animation deathAnimation) {
        this.deathAnimation = deathAnimation;
    }

    public Animation getSpawnAnimation() {
        return spawnAnimation;
    }

    @JsonSetter(nulls = Nulls.SKIP)
    public void setSpawnAnimation(Animation spawnAnimation) {
        this.spawnAnimation = spawnAnimation;
    }

    public SoundEffect getDeathSound() {
        return deathSound;
    }

    public void setDeathSound(SoundEffect deathSound) {
        this.deathSound = deathSound;
    }

    public SoundEffect getSpawnSound() {
        return spawnSound;
    }

    public void setSpawnSound(SoundEffect spawnSound) {
        this.spawnSound = spawnSound;
    }
}
