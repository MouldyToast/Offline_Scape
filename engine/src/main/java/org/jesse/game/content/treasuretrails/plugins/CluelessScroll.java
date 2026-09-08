package org.jesse.game.content.treasuretrails.plugins;

import org.jesse.game.item.ids.ItemId;
import org.jesse.game.model.item.pluginextensions.ItemPlugin;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.entity.masks.Animation;

/**
 * @author Kris | 05/01/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class CluelessScroll extends ItemPlugin {
    @Override
    public void handle() {
        bind("Read", (player, item, container, slotId) -> {
            player.setAnimation(new Animation(7403));
            WorldTasksManager.schedule(() -> player.sendMessage("You attempt to make out the text, but it makes no sense whatsoever."), 3);
        });
    }

    @Override
    public int[] getItems() {
        return new int[] {
                ItemId.CLUELESS_SCROLL
        };
    }
}
