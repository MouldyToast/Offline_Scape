package org.jesse.game.world.entity.npc.impl.slayer.superior.impl;

import org.jesse.game.content.skills.slayer.SlayerEquipment;
import org.jesse.game.world.Projectile;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.masks.Hit;
import org.jesse.game.world.entity.masks.HitType;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.npc.combat.CombatScript;
import org.jesse.game.world.entity.npc.impl.slayer.superior.SuperiorNPC;
import org.jesse.game.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 28/05/2019 01:59
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ScreamingTwistedBanshee extends SuperiorNPC implements CombatScript {
    public ScreamingTwistedBanshee(@NotNull final Player owner, @NotNull final NPC root, final Location tile) {
        super(owner, root, 7391, tile);
    }

    private static final Animation PLAYER_ANIMATION = new Animation(1572);
    private static final Projectile PROJECTILE = new Projectile(337, 80, 80);

    @Override
    public int attack(Entity target) {
        if (!(target instanceof Player)) {
            return 0;
        }
        final Player player = (Player) target;
        setAnimation(getCombatDefinitions().getAttackAnim());
        if (!SlayerEquipment.EARMUFFS.isWielding(player)) {
            delayHit(this, World.sendProjectile(this, target, PROJECTILE), player, new Hit(this, 9, HitType.REGULAR).onLand(hit -> {
                player.lock(3);
                player.setAnimation(PLAYER_ANIMATION);
                for (int i = 0; i <= 6; i++) {
                    if (i == 3) {
                        continue;
                    }
                    if (i == 5) {
                        player.getPrayerManager().setPrayerPoints((int) (player.getPrayerManager().getPrayerPoints() * 0.8020834));
                    } else {
                        player.getSkills().setLevel(i, (int) (player.getSkills().getLevel(i) * 0.8020834));
                    }
                }
            }));
        } else {
            delayHit(this, 0, player, new Hit(this, getRandomMaxHit(this, getCombatDefinitions().getMaxHit(), MELEE, target), HitType.MELEE));
        }
        return getCombatDefinitions().getAttackSpeed();
    }
}
