package org.jesse.game.world.entity.npc.impl.slayer.superior.impl;

import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.util.Utils;
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
import org.jesse.game.world.entity.player.action.combat.CombatUtilities;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 28/05/2019 02:21
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class NightBeast extends SuperiorNPC implements CombatScript {
    public NightBeast(@NotNull final Player owner, @NotNull final NPC root, final Location tile) {
        super(owner, root, 7409, tile);
    }

    private static final Projectile MAGICAL_PROJ = new Projectile(130, 140, 120, 40, 5, 38, 64, 5);
    private static final Graphics SPLASH = new Graphics(85, 0, 92);
    private static final Graphics GFX = new Graphics(131, 0, 92);

    private int areaAttacks = 0;

    @Override
    public int attack(final Entity target) {

        if(areaAttacks == 0 && Utils.random(5) == 1) {
            areaAttacks = 3;
        }

        if(areaAttacks > 0) {
            areaAttacks --;
            return sendGroupMagicAttack(target);
        }

        if (Utils.random(1) == 0 || !isWithinMeleeDistance(this, target)) {
            return sendMagicAttack(target);
        }
        this.setAnimation(this.getCombatDefinitions().getAttackAnim());
        delayHit(this, 0, target, new Hit(this, getRandomMaxHit(this, 31, MELEE, target), HitType.MELEE));
        return this.getCombatDefinitions().getAttackSpeed();
    }

    private int sendMagicAttack(final Entity target) {
        this.setAnimation(this.getCombatDefinitions().getAttackAnim());
        CombatUtilities.delayHit(this, World.sendProjectile(this, target, MAGICAL_PROJ), target, new Hit(this, CombatUtilities.getRandomMaxHit(this, 31, MAGIC, target), HitType.MAGIC).onLand(hit -> target.setGraphics(hit.getDamage() <= 0 ? SPLASH : GFX)));
        return this.getCombatDefinitions().getAttackSpeed();
    }

    private int sendGroupMagicAttack(final Entity target) {
        this.setAnimation(this.getCombatDefinitions().getAttackAnim());
        final Location tile = new Location(target.getLocation());
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                final Location t = tile.transform(x, y, 0);
                if (!World.isFloorFree(t, 1)) continue;
                World.sendProjectile(this, t, MAGICAL_PROJ);
                final int specificTime = MAGICAL_PROJ.getProjectileDuration(getLocation(), t);
                World.sendGraphics(new Graphics(GFX.getId(), GFX.getHeight(), specificTime), t);
            }
        }
        WorldTasksManager.schedule(() -> {
            if (target.getLocation().withinDistance(tile, 1)) {
                CombatUtilities.delayHit(this, -1, target, new Hit(this, (int) (target.getHitpoints() * 0.3F), HitType.MAGIC));
            }
        }, MAGICAL_PROJ.getTime(getLocation(), target.getLocation()));
        return this.getCombatDefinitions().getAttackSpeed();
    }
}
