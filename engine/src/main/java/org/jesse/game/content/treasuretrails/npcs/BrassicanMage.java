package org.jesse.game.content.treasuretrails.npcs;

import org.jesse.game.world.Projectile;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.ForceTalk;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.masks.Hit;
import org.jesse.game.world.entity.masks.HitType;
import org.jesse.game.world.entity.npc.combat.CombatScript;
import org.jesse.game.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 12/04/2019 19:34
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class BrassicanMage extends TreasureGuardian implements CombatScript {

    private static final ForceTalk chat = new ForceTalk("Grow up or leave.");
    private static final Projectile projectile = new Projectile(772, 40, 36, 21, 21, 11, 11, 5);

    public BrassicanMage(@NotNull final Player owner, @NotNull final Location tile) {
        super(owner, tile, 7310);
        setForceTalk(chat);
    }

    @Override
    public int attack(final Entity target) {
        setAnimation(combatDefinitions.getAttackAnim());
        delayHit(this, World.sendProjectile(this, target, projectile), target, new Hit(this, getRandomMaxHit(this, combatDefinitions.getMaxHit(), RANGED, target), HitType.REGULAR));
        return combatDefinitions.getAttackSpeed();
    }
}
