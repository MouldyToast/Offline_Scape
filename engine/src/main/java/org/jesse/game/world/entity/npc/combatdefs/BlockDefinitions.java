package org.jesse.game.world.entity.npc.combatdefs;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import org.jesse.game.world.entity.SoundEffect;
import org.jesse.game.world.entity.masks.Animation;

/**
 * @author Kris | 18/11/2018 02:53
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class BlockDefinitions {
    private Animation animation = Animation.STOP;
    private SoundEffect sound;

    public static BlockDefinitions construct(final BlockDefinitions clone) {
        final BlockDefinitions defs = new BlockDefinitions();
        if (clone == null) return defs;
        defs.animation = clone.animation;
        if (clone.sound != null) {
            defs.sound = new SoundEffect(clone.sound.getId(), clone.sound.getRadius(), clone.sound.getDelay() + 30);
        }
        return defs;
    }

    public Animation getAnimation() {
        return animation;
    }

    @JsonSetter(nulls = Nulls.SKIP)
    public void setAnimation(Animation animation) {
        this.animation = animation;
    }

    public SoundEffect getSound() {
        return sound;
    }

    public void setSound(SoundEffect sound) {
        this.sound = sound;
    }
}
