package org.jesse.game.world.entity.npc.impl.slayer;

import org.jesse.game.content.skills.slayer.SlayerEquipment;
import org.jesse.game.util.Direction;
import org.jesse.game.world.Projectile;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.SoundEffect;
import org.jesse.game.world.entity.masks.Graphics;
import org.jesse.game.world.entity.masks.Hit;
import org.jesse.game.world.entity.masks.HitType;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.npc.Spawnable;
import org.jesse.game.world.entity.npc.combat.CombatScript;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.calog.CAType;

/**
 * @author Kris | 20/08/2019 23:34
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class AberrantSpectre extends NPC implements Spawnable, CombatScript {

    public AberrantSpectre(final int id, final Location tile, final Direction facing, final int radius) {
        super(id, tile, facing, radius);
    }

    @Override
    public boolean validate(final int id, final String name) {
        return name.equalsIgnoreCase("aberrant spectre");
    }

    private static final Projectile PROJECTILE = new Projectile(335, 45, 38, 95, 30);
    private static final Graphics GRAPHICS = new Graphics(336, 0, 320);
    private static final SoundEffect impactSound = new SoundEffect(273, 10, 0);

    @Override protected void onFinish(Entity source) {
        super.onFinish(source);
        if (source instanceof Player) {
            ((Player) source).getCombatAchievements().complete(CAType.NOXIOUS_FOE);
        }
    }

    @Override
    public int attack(final Entity target) {
        final AberrantSpectre npc = this;
        if (!(target instanceof Player)) {
            return 0;
        }
        final Player player = (Player) target;
        npc.setAnimation(npc.getCombatDefinitions().getAttackAnim());
        attackSound();
        if (!SlayerEquipment.NOSE_PEG.isWielding(player)) {
            delayHit(npc, World.sendProjectile(npc, target, PROJECTILE), player, new Hit(npc, 14, HitType.REGULAR).onLand(hit -> {
                World.sendSoundEffect(new Location(player.getLocation()), impactSound);
                for (int i = 0; i <= 6; i++) {
                    if (i == 3) {
                        continue;
                    }
                    if (i == 5) {
                        player.getPrayerManager().setPrayerPoints((int) (player.getPrayerManager().getPrayerPoints() * 0.4105263157894737));
                    } else {
                        player.getSkills().setLevel(i, (int) (player.getSkills().getLevel(i) * 0.2105263157894737));
                    }
                }
                player.setGraphics(GRAPHICS);
            }));
        } else {
            delayHit(npc, World.sendProjectile(npc, target, PROJECTILE), player, new Hit(npc, getRandomMaxHit(npc, npc.getCombatDefinitions().getMaxHit(), MAGIC, target), HitType.MAGIC).onLand(hit -> {
                player.setGraphics(GRAPHICS);
                World.sendSoundEffect(new Location(player.getLocation()), impactSound);
            }));
        }
        return npc.getCombatDefinitions().getAttackSpeed();
    }
}
