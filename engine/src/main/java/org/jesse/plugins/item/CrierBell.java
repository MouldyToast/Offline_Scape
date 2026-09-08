package org.jesse.plugins.item;

import org.jesse.game.item.ids.ItemId;
import org.jesse.game.model.item.pluginextensions.ItemPlugin;
import org.jesse.game.world.entity.masks.Animation;

/**
 * @author Kris | 19/02/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class CrierBell extends ItemPlugin {
    @Override
    public void handle() {
        bind("Ring", (player, item, slotId) -> player.setAnimation(new Animation(7268)));
    }

    @Override
    public int[] getItems() {
        return new int[] {
                ItemId.CRIER_BELL
        };
    }
}
