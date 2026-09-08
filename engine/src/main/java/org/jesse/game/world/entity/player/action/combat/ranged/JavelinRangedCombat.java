package org.jesse.game.world.entity.player.action.combat.ranged;

import org.jesse.game.world.Projectile;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.masks.Graphics;
import org.jesse.game.world.entity.masks.Hit;
import org.jesse.game.world.entity.player.action.combat.RangedCombat;

/**
 * @author Kris | 16/01/2019 20:44
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class JavelinRangedCombat extends RangedCombat {

    public JavelinRangedCombat(final Entity target) {
        super(target);
    }

    @Override
    public void extra(Hit hit) {
        final Projectile projectile = ammunitionSource.getAmmunitionDefinition().getProjectile();
        final int clientCycles = projectile.getProjectileDuration(player.getLocation(), target.getLocation());
        target.setGraphics(new Graphics(344, clientCycles, 146));
    }

}
