package org.jesse.game.world.entity.npc.impl.slayer.wyverns;

import org.jesse.game.content.achievementdiary.diaries.FaladorDiary;
import org.jesse.game.util.Direction;
import org.jesse.game.util.Utils;
import org.jesse.game.world.Projectile;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.masks.Graphics;
import org.jesse.game.world.entity.masks.Hit;
import org.jesse.game.world.entity.masks.HitType;
import org.jesse.game.world.entity.npc.combatdefs.AttackType;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.calog.CAType;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

/**
 * @author Tommeh | 17 apr. 2018 | 19:44:29
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class SkeletalWyvern extends Wyvern {
    private static final Animation icyBreathAnim = new Animation(2985);
    private static final Graphics icyBreathGfx = new Graphics(501);
    private static final Animation rangedAnim = new Animation(2989);
    private static final Graphics rangedGfx = new Graphics(499);
    private static final Projectile rangedProjectile = new Projectile(500, 300, 140, 50, 0, 8, 0, 5);
    private static final AttackType[] ATTACK_STYLES = {AttackType.SLASH, AttackType.MAGIC, AttackType.RANGED};

    public SkeletalWyvern(final int id, final Location tile, final Direction facing, final int radius) {
        super(id, tile, facing, radius);
    }

    @Override
    public int attack(final Entity target) {
        switch (getCombatDefinitions().getAttackStyle()) {
        case MAGIC: 
            magicAttack(target);
            break;
        case RANGED: 
            attackSound();
            setAnimation(rangedAnim);
            setGraphics(rangedGfx);
            delayHit(this, World.sendProjectile(this, target, rangedProjectile), target, new Hit(this, getRandomMaxHit(this, getCombatDefinitions().getMaxHit(), RANGED, target), HitType.RANGED).onLand(h -> World.sendSoundEffect(new Location(target.getLocation()), impactSound)));
            break;
        default: 
            setAnimation(getCombatDefinitions().getAttackAnim());
            delayHit(this, 0, target, new Hit(this, getRandomMaxHit(this, getCombatDefinitions().getMaxHit(), MELEE, target), HitType.MELEE));
            break;
        }
        final AttackType currentStyle = combatDefinitions.getAttackStyle();
        final ObjectArrayList<AttackType> styles = new ObjectArrayList<AttackType>(Arrays.asList(ATTACK_STYLES));
        styles.remove(currentStyle);
        combatDefinitions.setAttackStyle(styles.get(Utils.random(styles.size() - 1)));
        return getCombatDefinitions().getAttackSpeed();
    }

    @Override
    public void onDeath(final Entity source) {
        super.onDeath(source);
        if (source instanceof Player) {
            final Player player = (Player) source;
            player.getAchievementDiaries().update(FaladorDiary.KILL_SKELETAL_WYVERN);
            player.getCombatAchievements().complete(CAType.A_FROZEN_FOE_FROM_THE_PAST);
        }
    }

    @Override
    public boolean validate(final int id, final String name) {
        return name.equals("skeletal wyvern");
    }

    @Override
    protected String notificationName(@NotNull final Player player) {
        return "skeletal wyvern";
    }

    @Override
    protected Animation getIcyBreathAnim() {
        return icyBreathAnim;
    }

    @Override
    protected Graphics getIcyBreathGfx() {
        return icyBreathGfx;
    }
}
