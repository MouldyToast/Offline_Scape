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
 * @author Kris | 12/04/2019 16:34
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ArmadylianGuard extends TreasureGuardian implements CombatScript {

    private static final ForceTalk chat = new ForceTalk("No warning! Begone!");
    private static final Projectile projectile = new Projectile(1193, 85, 30, 30, 15, 8, 0, 5);

    public ArmadylianGuard(@NotNull final Player owner, @NotNull final Location tile) {
        super(owner, tile, 6587);
        setForceTalk(chat);
    }

    @Override
    public boolean isAttackable(final Entity e) {
        if (!owner.equals(e)) {
            if (e instanceof Player) {
                ((Player) e).sendMessage("You cannot attack that.");
            }
            return false;
        }
        return true;
    }

    @Override
    public int attack(final Entity target) {
        setAnimation(combatDefinitions.getAttackAnim());
        delayHit(this, World.sendProjectile(this, target, projectile), target, new Hit(this, getRandomMaxHit(this, combatDefinitions.getMaxHit(), RANGED, target), HitType.RANGED));
        return combatDefinitions.getAttackSpeed();
    }
}
