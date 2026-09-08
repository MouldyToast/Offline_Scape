package org.jesse.plugins.item;

import org.jesse.game.model.item.pluginextensions.ItemPlugin;

import static org.jesse.game.item.ids.ItemId.ARCLIGHT;

/**
 * @author Tommeh | 31-1-2019 | 19:58
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class Arclight extends ItemPlugin {

    public static final int MAX_CHARGES = 10_000;

    @Override
    public void handle() {
        bind("Check", (player, item, slotId) -> {
            final int charges = item.getCharges();
            player.sendMessage(charges == 0 ? "Your arclight has run out of charges." : "Your arclight has " + charges + " charges left.");
        });
    }

    @Override
    public int[] getItems() {
        return new int[] {ARCLIGHT};
    }
}
