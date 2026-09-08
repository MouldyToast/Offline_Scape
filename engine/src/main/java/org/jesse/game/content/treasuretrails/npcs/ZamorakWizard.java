package org.jesse.game.content.treasuretrails.npcs;

import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.ForceTalk;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.npc.combat.CombatScript;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.action.combat.magic.CombatSpell;
import org.jetbrains.annotations.NotNull;

/**
 * @author Kris | 28/02/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ZamorakWizard extends TreasureGuardian implements CombatScript {

    private static final ForceTalk forceTalk = new ForceTalk("Die, human!");

    public ZamorakWizard(@NotNull Player owner, @NotNull Location tile) {
        super(owner, tile, 2954);
        setForceTalk(forceTalk);
    }

    @Override
    public int attack(Entity target) {
        useSpell(CombatSpell.FLAMES_OF_ZAMORAK, target, combatDefinitions.getMaxHit());
        return combatDefinitions.getAttackSpeed();
    }
}
