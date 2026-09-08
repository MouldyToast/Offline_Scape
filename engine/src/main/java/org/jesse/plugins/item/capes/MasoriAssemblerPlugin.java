package org.jesse.plugins.item.capes;

import org.jesse.game.item.ids.ItemId;
import org.jesse.game.model.item.pluginextensions.ItemDeathStatus;
import org.jesse.game.model.item.pluginextensions.ItemPlugin;

@SuppressWarnings("unused")
public class MasoriAssemblerPlugin extends ItemPlugin implements ItemPlugin.ItemStatusOnDeath {
    @Override
    public ItemDeathStatus getStatus() {
        return ItemDeathStatus.KEEP_ON_DEATH;
    }

    @Override
    public void handle() {

    }

    @Override
    public int[] getItems() {
        return new int[]{ItemId.MASORI_ASSEMBLER};
    }
}
