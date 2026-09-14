package org.jesse.game.content.minigame.fightcaves.npcs;

import org.jesse.game.content.achievementdiary.diaries.KaramjaDiary;
import org.jesse.game.content.minigame.fightcaves.FightCaves;
import org.jesse.game.util.Utils;
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
import org.jesse.game.world.entity.npc.combatdefs.AttackType;
import org.jesse.game.world.entity.player.Player;

/**
 * @author Kris | 27/10/2018 17:43
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
final class KetZek extends FightCavesNPC implements CombatScript {
    private static final Animation MELEE_ANIM = new Animation(2644);
    private static final Animation MAGIC_ANIM = new Animation(2647);
    private static final Graphics GFX = new Graphics(446);
    private static final Projectile MAGIC_PROJ = new Projectile(445, 480, 80, 40, 5, 10, 0, 5);
    private static final SoundEffect attackSound = new SoundEffect(598);

    KetZek(final TzHaarNPC npc, final Location tile, final FightCaves caves) {
        super(npc, tile, caves);
        setAttackDistance(15);
    }

    @Override
    public void onDeath(final Entity source) {
        super.onDeath(source);
        if (source instanceof Player) {
            final Player player = (Player) source;
            player.getAchievementDiaries().update(KaramjaDiary.KILL_KET_ZEK);
        }
    }

    @Override
    public int attack(Entity target) {
        final int style = Utils.random(1);
        playSound(attackSound);
        if (style == 0 && isWithinMeleeDistance(this, target)) {
            setAnimation(MELEE_ANIM);
            delayHit(0, target, new Hit(this, getRandomMaxHit(this, 49, AttackType.STAB, target), HitType.MELEE));
        } else {
            setAnimation(MAGIC_ANIM);
            delayHit(World.sendProjectile(this, target, MAGIC_PROJ), target, new Hit(this, getRandomMaxHit(this, 49, MAGIC, target), HitType.MAGIC).onLand(hit -> target.setGraphics(GFX)));
        }
        return combatDefinitions.getAttackSpeed();
    }
}
