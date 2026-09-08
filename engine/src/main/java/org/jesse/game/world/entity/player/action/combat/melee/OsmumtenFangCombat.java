package org.jesse.game.world.entity.player.action.combat.melee;

import org.jesse.game.util.Utils;
import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.masks.HitType;
import org.jesse.game.world.entity.npc.combatdefs.AttackType;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.action.combat.CombatUtilities;
import org.jesse.game.world.entity.player.action.combat.MeleeCombat;

public class OsmumtenFangCombat extends MeleeCombat {

    public OsmumtenFangCombat(Entity target) {
        super(target);
    }

    @Override
    public int getRandomHit(Player player, Entity target, int maxhit, double modifier, AttackType attackType) {
        if (CombatUtilities.isAlwaysTakeMaxHit(target, HitType.MELEE)) {
            return maxhit;
        }

        final int accuracy = getAccuracy(player, target, modifier);
        final int targetRoll = getTargetDefenceRoll(player, target, attackType);
        sendDebug(accuracy, targetRoll, maxhit);
        int accRoll = Utils.random(accuracy);
        int defRoll = Utils.random(targetRoll);
        if (accRoll <= defRoll) {
            accRoll = Utils.random(accuracy);
            //if monster.hasAttribute("toa")://TODO do we add the ToA reroll??
            defRoll = Utils.random(targetRoll);
            if (accRoll <= defRoll) {
                return 0;
            }
        }

        int fifteen = maxhit * 15 / 100;
        return fifteen + Utils.random(maxhit - fifteen * (player.getCombatDefinitions().isUsingSpecial() ? 1 : 2));
    }

}
