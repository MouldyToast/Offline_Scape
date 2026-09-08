package org.jesse.game.content.trouver;

import org.jesse.game.model.item.pluginextensions.ItemPlugin;

public class TrouverItemDeathPlugin extends ItemPlugin {

    @Override
    public void handle() {

    }

    @Override
    public int[] getItems() {
        return TrouverData.PROTECTED_ITEMS;
    }


}
