package org.jesse.plugins.item;

import org.jesse.game.model.item.SkillcapePerk;
import org.jesse.game.model.item.pluginextensions.ItemPlugin;
import org.jesse.game.world.entity.player.variables.TickVariable;
import org.jesse.utils.TimeUnit;

/**
 * @author Kris | 16/03/2019 02:54
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class AgilityCape extends ItemPlugin {
    @Override
    public void handle() {
        bind("Stamina Boost", (player, item, slotId) -> {
            final Number lastConsumptionDate = player.getNumericAttribute("Stamina Boost Use");
            final long milliseconds = lastConsumptionDate.longValue();
            if (TimeUnit.MILLISECONDS.toHours(System.currentTimeMillis() - milliseconds) < 24) {
                player.sendMessage("You have used your stamina boost for today. Try again tomorrow.");
                return;
            }
            player.addAttribute("Stamina Boost Use", System.currentTimeMillis());
            player.sendMessage("You feel reinvigorated.");
            player.getVariables().setRunEnergy(100);
            player.getVarManager().sendBit(25, 1);
            player.getVarManager().sendBit(24, 100);
            player.getVariables().schedule(100, TickVariable.STAMINA_ENHANCEMENT);
        });
    }

    @Override
    public int[] getItems() {
        return SkillcapePerk.AGILITY.getSkillCapes();
    }
}
