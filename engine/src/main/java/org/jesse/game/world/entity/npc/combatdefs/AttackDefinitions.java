package org.jesse.game.world.entity.npc.combatdefs;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import org.jesse.game.world.Projectile;
import org.jesse.game.world.entity.SoundEffect;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.masks.Graphics;

/**
 * @author Kris | 18/11/2018 02:52
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class AttackDefinitions {
    //TODO don't forget to update this on construction.
    private transient AttackType defaultMeleeType = AttackType.CRUSH;
    private AttackType type = AttackType.CRUSH;
    private int maxHit;
    private Animation animation = Animation.STOP;
    private SoundEffect startSound;
    private SoundEffect impactSound;
    private Projectile projectile;
    private Graphics impactGraphics;
    private Graphics drawbackGraphics;

    public static AttackDefinitions construct(final AttackDefinitions clone) {
        final AttackDefinitions defs = new AttackDefinitions();
        if (clone == null) {
            return defs;
        }
        defs.type = clone.type;
        defs.maxHit = clone.maxHit;
        defs.animation = clone.animation;
        defs.startSound = clone.startSound;
        defs.impactSound = clone.impactSound;
        defs.projectile = clone.projectile;
        defs.impactGraphics = clone.impactGraphics;
        defs.drawbackGraphics = clone.drawbackGraphics;
        if (clone.type.isMelee()) {
            defs.defaultMeleeType = clone.type;
        }
        return defs;
    }

    public AttackType getDefaultMeleeType() {
        return defaultMeleeType;
    }

    public void setDefaultMeleeType(AttackType defaultMeleeType) {
        this.defaultMeleeType = defaultMeleeType;
    }

    public AttackType getType() {
        return type;
    }

    @JsonSetter(nulls = Nulls.SKIP)
    public void setType(AttackType type) {
        this.type = type;
    }

    public int getMaxHit() {
        return maxHit;
    }

    public void setMaxHit(int maxHit) {
        this.maxHit = maxHit;
    }

    public Animation getAnimation() {
        return animation;
    }

    @JsonSetter(nulls = Nulls.SKIP)
    public void setAnimation(Animation animation) {
        this.animation = animation;
    }

    public SoundEffect getStartSound() {
        return startSound;
    }

    public void setStartSound(SoundEffect startSound) {
        this.startSound = startSound;
    }

    public SoundEffect getImpactSound() {
        return impactSound;
    }

    public void setImpactSound(SoundEffect impactSound) {
        this.impactSound = impactSound;
    }

    public Projectile getProjectile() {
        return projectile;
    }

    public void setProjectile(Projectile projectile) {
        this.projectile = projectile;
    }

    public Graphics getImpactGraphics() {
        return impactGraphics;
    }

    public void setImpactGraphics(Graphics impactGraphics) {
        this.impactGraphics = impactGraphics;
    }

    public Graphics getDrawbackGraphics() {
        return drawbackGraphics;
    }

    public void setDrawbackGraphics(Graphics drawbackGraphics) {
        this.drawbackGraphics = drawbackGraphics;
    }
}
