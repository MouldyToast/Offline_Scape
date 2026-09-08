package org.jesse.plugins.item;

import org.jesse.game.item.ids.ItemId;
import org.jesse.game.model.item.degradableitems.ChargesManager;
import org.jesse.game.model.item.pluginextensions.ItemPlugin;
import org.jesse.plugins.itemonitem.InfernalToolInfusion;

/**
 * @author Kris | 30/08/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class InfernalTool extends ItemPlugin {
    @Override
    public void handle() {
        bind("Check", (player, item, container, slotId) ->
                player.sendMessage(item.getName() + ": " + ChargesManager.FORMATTER.format((item.getCharges() / (float) InfernalToolInfusion.CHARGES) * 100F) + "% remaining."));
    }

    @Override
    public int[] getItems() {
        return new int[] {
                ItemId.INFERNAL_AXE, ItemId.INFERNAL_PICKAXE, ItemId.INFERNAL_HARPOON
        };
    }
}
