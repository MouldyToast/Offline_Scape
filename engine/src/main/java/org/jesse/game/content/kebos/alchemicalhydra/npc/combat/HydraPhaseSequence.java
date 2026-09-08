package org.jesse.game.content.kebos.alchemicalhydra.npc.combat;

import org.jesse.game.content.kebos.alchemicalhydra.npc.AlchemicalHydra;
import org.jesse.game.util.Utils;
import org.jesse.game.world.Projectile;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.Toxins;
import org.jesse.game.world.entity.masks.Hit;
import org.jesse.game.world.entity.masks.HitType;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.action.combat.CombatUtilities;

/**
 * @author Tommeh | 02/11/2019 | 19:58
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public interface HydraPhaseSequence {
    Projectile magicAttackProj = new Projectile(1662, 22, 12, 30, 10, 30, 0, 5);
    Projectile rangedAttackProj = new Projectile(1663, 42, 32, 30, 2, 15, 0, 5);

    int autoAttack(final AlchemicalHydra hydra, final Player player);

    boolean attack(final AlchemicalHydra hydra, final Player player);

    void process(final AlchemicalHydra hydra, final Player player);

    default void applyPoison(final AlchemicalHydra hydra, final Player player) {
        //Prevent the attack from going through if the player has teleported off.
        final Location nextLocation = player.getNextLocation();
        if (nextLocation != null && !hydra.getInstance().inside(nextLocation)) {
            return;
        }
        CombatUtilities.delayHit(hydra, -1, player, new Hit(hydra, Utils.random(5, 12), HitType.POISON).onLand(hit -> player.getToxins().applyToxin(Toxins.ToxinType.POISON, 6, hydra)));
    }
}
