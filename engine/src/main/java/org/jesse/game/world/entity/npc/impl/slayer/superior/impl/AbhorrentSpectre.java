package org.jesse.game.world.entity.npc.impl.slayer.superior.impl;

import org.jesse.game.content.skills.slayer.SlayerEquipment;
import org.jesse.game.world.Projectile;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.masks.Graphics;
import org.jesse.game.world.entity.masks.Hit;
import org.jesse.game.world.entity.masks.HitType;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.npc.combat.CombatScript;
import org.jesse.game.world.entity.npc.impl.slayer.superior.SuperiorNPC;
import org.jesse.game.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 28/05/2019 02:12
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class AbhorrentSpectre extends SuperiorNPC implements CombatScript {
    public AbhorrentSpectre(@NotNull final Player owner, @NotNull final NPC root, final Location tile) {
        super(owner, root, 7402, tile);
    }

    private static final Projectile PROJECTILE = new Projectile(335, 85, 38, 45, 0, 95, 64, 5);
    private static final Graphics GRAPHICS = new Graphics(336, 0, 320);

    @Override
    public int attack(final Entity target) {
        final Player player = (Player) target;
        setAnimation(getCombatDefinitions().getAttackAnim());
            if (!SlayerEquipment.NOSE_PEG.isWielding(player)) {
                delayHit(this, World.sendProjectile(this, target, PROJECTILE), player, new Hit(this, 31, HitType.REGULAR).onLand(hit -> {
                    for (int i = 0; i <= 6; i++) {
                        if (i == 3) {
                            continue;
                        }
                        if (i == 5) {
                            player.getPrayerManager()
                                    .setPrayerPoints((int) (player.getPrayerManager().getPrayerPoints() * 0.2105263157894737));
                        } else {
                            player.getSkills().setLevel(i, (int) (player.getSkills().getLevel(i) * 0.2105263157894737));
                        }
                    }
                    player.setGraphics(GRAPHICS);
                }));
            } else {
                delayHit(this, World.sendProjectile(this, target, PROJECTILE), player,
                        new Hit(this, getRandomMaxHit(this, this.getCombatDefinitions().getMaxHit(), MAGIC, target), HitType.MAGIC).onLand(hit -> player.setGraphics(GRAPHICS)));
            }
        return this.getCombatDefinitions().getAttackSpeed();
    }
}
