package org.jesse.game.world.entity.player.action.combat.melee;

import org.jesse.game.world.entity.Entity;
import org.jesse.game.world.entity.masks.Hit;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.npc.combatdefs.AttackType;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.action.combat.MeleeCombat;

public class IvandisFlailCombat extends MeleeCombat {

    public IvandisFlailCombat(Entity target) {
        super(target);
    }


    @Override
    public Hit getHit(Player player, Entity target, double accuracyModifier, double passiveModifier, double activeModifier, boolean ignorePrayers) {
        if(target instanceof NPC && isVampyre((NPC) target)) {
            activeModifier += 0.2D;
        }
        return super.getHit(player, target, accuracyModifier, passiveModifier, activeModifier, ignorePrayers);
    }

    @Override
    public int getRandomHit(Player player, Entity target, int maxhit, double modifier, AttackType attackType) {
        return super.getRandomHit(player, target, maxhit, modifier, attackType);
    }

    public boolean isVampyre(NPC npc) {
        String name = npc.getName();
        return name.equalsIgnoreCase("Vampyre Juvinate") || name.equalsIgnoreCase("Vampyre Juvenile")
                || name.equalsIgnoreCase("Vyrewatch Sentinel") || name.equalsIgnoreCase("Vyre") || name.equalsIgnoreCase("Vyrewatch");
    }
}
