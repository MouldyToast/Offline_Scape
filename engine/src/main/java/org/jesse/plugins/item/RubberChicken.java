package org.jesse.plugins.item;

import org.jesse.game.item.ids.ItemId;
import org.jesse.game.model.item.pluginextensions.ItemPlugin;
import org.jesse.game.world.entity.masks.Animation;

/**
 * @author Chris
 * @since August 18 2020
 */
public class RubberChicken extends ItemPlugin {
    private static final Animation danceAnim = new Animation(1835);

    @Override
    public void handle() {
        bind("Dance", (player, item, slotId) -> player.setAnimation(danceAnim));
    }

    @Override
    public int[] getItems() {
        return new int[] {ItemId.RUBBER_CHICKEN};
    }
}
