package org.jesse.plugins.item;

import org.jesse.game.model.item.ItemActionHandler;
import org.jesse.game.model.item.pluginextensions.ItemPlugin;
import org.jesse.game.world.region.area.wilderness.WildernessArea;

/**
 * @author Kris | 03/07/2019 17:34
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class MysteriousEmblemItem extends ItemPlugin {
    @Override
    public void handle() {
        setDefault("Destroy", (player, item, slotId) -> {
            if (WildernessArea.isWithinWilderness(player) && player.isUnderCombat()) {
                player.sendMessage("You cannot destroy the emblems while being attacked in Wilderness.");
                return;
            }
            ItemActionHandler.dropItem(player, "Destroy", slotId, 300, 500);
        });
    }

    @Override
    public int[] getItems() {
        return new int[] {
                12746, 12748, 12749, 12750, 12751, 12752, 12753, 12754, 12755, 12756
        };
    }
}
