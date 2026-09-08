package org.jesse.game.content.breaches.entity.impl;

import org.jesse.game.content.breaches.entity.BreachEntity;
import org.jesse.game.content.skills.prayer.Prayer;
import org.jesse.game.util.Direction;
import org.jesse.game.util.Utils;
import org.jesse.game.world.Projectile;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.*;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.masks.Hit;
import org.jesse.game.world.entity.masks.HitType;
import org.jesse.game.world.entity.npc.Spawnable;
import org.jesse.game.world.entity.npc.combat.CombatScript;
import org.jesse.game.world.entity.npc.combatdefs.AttackType;
import org.jesse.game.world.entity.player.Player;

import static org.jesse.game.npc.ids.NpcId.KRIL_TSUTSAROTH_12446;

public class KrilTsutsaroth extends BreachEntity implements Spawnable, CombatScript {
    private static final Animation meleeAnimation = new Animation(6948);
    private static final Animation magicAnimation = new Animation(6950);
    private static final Projectile projectile = new Projectile(1155, 41, 16, 30, 5, 10, 0, 5);
    private static final ForceTalk forceTalk = new ForceTalk("YARRRRRRR!");
    private static final String message = "K'ril Tsutsaroth slams through your protection prayer, leaving you feeling drained.";
    private static final SoundEffect magicSound = new SoundEffect(3866, 10, 0);

    public KrilTsutsaroth(int id, Location tile, Direction facing, int radius) {
        super(id, tile, facing, radius);
    }


    @Override
    public boolean validate(final int id, final String name) {
        return id == KRIL_TSUTSAROTH_12446;
    }


    @Override
    public int attack(final Entity target) {
        final int style = Utils.random(15);
        if (style == 15 && target instanceof Player player && player.getPrayerManager().isActive(Prayer.PROTECT_FROM_MELEE)) {
            setAnimation(magicAnimation);
            setForceTalk(forceTalk);
            player.sendMessage(message);
            player.getPrayerManager().drainPrayerPoints(player.getPrayerManager().getPrayerPoints() / 2);
            delayHit(this, 0, target, new Hit(this, getRandomMaxHit(this, 49, MELEE, target), HitType.REGULAR));
        }
        else if (style < 10) {
            setAnimation(meleeAnimation);
            delayHit(this, 0, target, new Hit(this, getRandomMaxHit(this, 47, AttackType.SLASH, target), HitType.MELEE));
            target.getToxins().applyToxin(Toxins.ToxinType.POISON, 16, this);
        }
        else {
            World.sendSoundEffect(getMiddleLocation(), magicSound);
            setAnimation(magicAnimation);
            for (final Entity t : getPossibleTargets(EntityType.PLAYER)) {
                World.sendProjectile(this, t, projectile);
                int damage = getRandomMaxHit(this, 30, MAGIC, t);
                if (damage > 0) {
                    damage = Utils.random(10, 30);
                }
                //k'ril deals a minimum of 10 damage upon successful hit; for even distribution, we re-calc it.
                delayHit(this, projectile.getTime(this, t), t, new Hit(this, damage, HitType.MAGIC));
            }
        }
        return getCombatDefinitions().getAttackSpeed();
    }
}
