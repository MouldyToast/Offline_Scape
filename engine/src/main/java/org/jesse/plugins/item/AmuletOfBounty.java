package org.jesse.plugins.item;

import org.jesse.game.model.item.ItemActionHandler;
import org.jesse.game.model.item.pluginextensions.ItemPlugin;

/**
 * @author Kris | 09/10/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class AmuletOfBounty extends ItemPlugin {
    @Override
    public void handle() {
        bind("Check", (player, item, container, slotId) -> {
            final int uses = player.getNumericAttribute("amulet of bounty uses").intValue();
            player.sendMessage("Your amulet of bounty has " + ((10 - uses)) + " charge" + (uses == 9 ? "" : "s") + " left.");
        });
        bind("Break", (player, item, container, slotId) -> ItemActionHandler.dropItem(player, "Destroy", slotId, 300, 500));
    }

    @Override
    public int[] getItems() {
        return new int[] {21160};
    }
}
