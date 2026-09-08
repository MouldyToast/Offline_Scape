package org.jesse.plugins.item;

import org.jesse.game.content.skills.magic.spells.teleports.TeleportCollection;
import org.jesse.game.item.ids.ItemId;
import org.jesse.game.model.item.pluginextensions.ItemPlugin;

/**
 * @author Kris | 23/04/2019 23:10
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
@SuppressWarnings("unused")
public final class MythicalCape extends ItemPlugin {

    @Override
    public void handle() {
        bind("Teleport", (player, item, slotId) -> TeleportCollection.MYTHICAL_CAPE.teleport(player));
    }

    @Override
    public int[] getItems() {
        return new int[] {
                ItemId.MYTHICAL_CAPE_22114, ItemId.MYTHICAL_MAX_CAPE
        };
    }
}
