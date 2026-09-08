package org.jesse.game.world.entity.npc.impl.slayer;

import org.jesse.game.content.skills.slayer.SlayerEquipment;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.util.Direction;
import org.jesse.game.world.Projectile;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.masks.Hit;
import org.jesse.game.world.entity.masks.HitType;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.npc.Spawnable;
import org.jesse.game.world.entity.npc.combat.CombatScript;
import org.jesse.game.world.entity.player.Player;

/**
 * @author Kris | 20/08/2019 23:35
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class Basilisk extends NPC implements Spawnable, CombatScript {
    public Basilisk(final int id, final Location tile, final Direction facing, final int radius) {
        super(id, tile, facing, radius);
    }

    @Override
    public boolean validate(final int id, final String name) {
        return name.equalsIgnoreCase("basilisk");
    }

    private static final Projectile PROJECTILE = new Projectile(75, 18, 25, 115, 0);

    @Override
    public int attack(final Entity target) {
        final Basilisk npc = this;
        if (!(target instanceof Player)) {
            return 0;
        }
        final Player player = (Player) target;
        npc.setAnimation(npc.getCombatDefinitions().getAttackAnim());
        attackSound();
        if (!SlayerEquipment.MIRROR_SHIELD.isWielding(player)) {
            delayHit(npc, 0, player, new Hit(npc, 11, HitType.REGULAR));
            WorldTasksManager.schedule(() -> {
                if (!player.getLocation().withinDistance(target.getLocation(), 15)) {
                    return;
                }
                for (int i = 0; i <= 6; i++) {
                    if (i == 3) {
                        continue;
                    }
                    if (i == 5) {
                        player.getPrayerManager().setPrayerPoints((int) (player.getPrayerManager().getPrayerPoints() * 0.4078947368421053));
                    } else {
                        player.getSkills().setLevel(i, (int) (player.getSkills().getLevel(i) * 0.4078947368421053));
                    }
                }
            }, World.sendProjectile(npc, target, PROJECTILE));
        } else {
            delayHit(npc, 0, player, new Hit(npc, getRandomMaxHit(npc, npc.getCombatDefinitions().getMaxHit(), MELEE, target), HitType.MELEE));
        }
        return npc.getCombatDefinitions().getAttackSpeed();
    }
}
