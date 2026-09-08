package org.jesse.game.content.chambersofxeric.plugins.item;

import org.jesse.game.item.Item;
import org.jesse.game.model.item.pluginextensions.ItemPlugin;
import org.jesse.game.world.entity.SoundEffect;

/**
 * @author Kris | 06/08/2019 13:05
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class WaterFilledGourd extends ItemPlugin {

    /**
     * The empty gourd item id.
     */
    private static final int EMPTY_GOURD = 20800;

    /**
     * The water-filled gourd item id.
     */
    private static final int WATER_FILLED_GOURD = 20801;

    /**
     * The sound effect played when you pour the water out of a water-filled gourd.
     */
    private static final SoundEffect pouringWaterSound = new SoundEffect(2401);

    @Override
    public void handle() {
        bind("Empty", (player, item, container, slotId) -> {
            player.getInventory().set(slotId, new Item(EMPTY_GOURD));
            player.sendSound(pouringWaterSound);
        });
    }

    @Override
    public int[] getItems() {
        return new int[]{
                WATER_FILLED_GOURD
        };
    }
}
