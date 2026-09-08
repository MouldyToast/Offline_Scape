package org.jesse.game.world.entity.player.action.combat.melee;

import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.action.combat.MeleeCombat;

public class ViggoraChainmaceCombat extends MeleeCombat {
    public ViggoraChainmaceCombat(Entity target) {
        super(target);
    }

    @Override
    public int getAccuracy(Player player, Entity target, double resultModifier) {
        int baseAccuracy = super.getAccuracy(player, target, resultModifier);
        if(player.getWeapon().getCharges() > 1 && target instanceof NPC && ((NPC) target).isInWilderness()) {
            return (int) (baseAccuracy * 1.5);
        }
        return baseAccuracy;
    }

    @Override
    public int getMaxHit(Player player, double passiveModifier, double activeModifier, boolean ignorePrayers) {
        int baseMaxHit = super.getMaxHit(player, passiveModifier, activeModifier, ignorePrayers);

        if(player.getWeapon().getCharges() > 1 && target instanceof NPC && ((NPC) target).isInWilderness()) {
            return (int) (baseMaxHit * 1.5);
        }
        return baseMaxHit;
    }
}
