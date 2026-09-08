package org.jesse.game.world.entity.npc.impl.slayer.superior.impl;

import org.jesse.game.content.skills.slayer.SlayerEquipment;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.Projectile;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.masks.Hit;
import org.jesse.game.world.entity.masks.HitType;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.npc.combat.CombatScript;
import org.jesse.game.world.entity.npc.impl.slayer.superior.SuperiorNPC;
import org.jesse.game.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 28/05/2019 02:03
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class Cockathrice extends SuperiorNPC implements CombatScript {
    public Cockathrice(@NotNull final Player owner, @NotNull final NPC root, final Location tile) {
        super(owner, root, 7393, tile);
    }
    private static final Projectile PROJECTILE = new Projectile(75, 18, 25, 115, 0);

    @Override
    public int attack(final Entity target) {
        if (!(target instanceof Player)) {
            return 0;
        }
        final Cockathrice npc = this;
        final Player player = (Player) target;
        npc.setAnimation(npc.getCombatDefinitions().getAttackAnim());
        attackSound();
        if (!SlayerEquipment.MIRROR_SHIELD.isWielding(player)) {
            delayHit(npc, 0, player, new Hit(npc, 10, HitType.REGULAR));
            WorldTasksManager.schedule(() -> {
                if (!player.getLocation().withinDistance(target, 15)) {
                    return;
                }
                for (int i = 0; i <= 6; i++) {
                    if (i == 3) {
                        continue;
                    }
                    if (i == 5) {
                        player.getPrayerManager().setPrayerPoints((int) (player.getPrayerManager().getPrayerPoints() * 0.6052631578947368));
                    } else {
                        player.getSkills().setLevel(i, (int) (player.getSkills().getLevel(i) * 0.6052631578947368));
                    }
                }
            }, World.sendProjectile(npc, target, PROJECTILE));
        } else {
            delayHit(npc, 0, player, new Hit(npc, getRandomMaxHit(npc, npc.getCombatDefinitions().getMaxHit(), MELEE, target), HitType.MELEE));
        }
        return npc.getCombatDefinitions().getAttackSpeed();
    }
}
