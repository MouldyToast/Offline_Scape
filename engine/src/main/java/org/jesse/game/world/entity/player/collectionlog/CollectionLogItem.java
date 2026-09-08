package org.jesse.game.world.entity.player.collectionlog;

import org.jesse.game.GameInterface;
import org.jesse.game.model.item.pluginextensions.ItemPlugin;

/**
 * @author Kris | 24/03/2019 15:11
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class CollectionLogItem extends ItemPlugin {
    @Override
    public void handle() {
        bind("Read", (player, item, slotId) -> GameInterface.COLLECTION_LOG.open(player));
    }

    @Override
    public int[] getItems() {
        return new int[] {
                22711
        };
    }
}
