package com.zenyte.game.model.item.actions;

import com.zenyte.game.model.item.pluginextensions.ItemPlugin;

import static com.near_reality.game.item.CustomItemId.OMEGA_BOOTS;
import static com.near_reality.game.item.CustomItemId.OMEGA_BOOTS_32607;

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2025-01-23
 */
public class OmegaBoots extends ItemPlugin {
    @Override
    public void handle() {
        bind("Toggle", (player, item, slotId) -> {
            var normalBoots = item.getId() == OMEGA_BOOTS;
            if (normalBoots)
                item.setId(OMEGA_BOOTS_32607);
            else
                item.setId(OMEGA_BOOTS);
            player.getInventory().refresh(slotId);
        });
    }

    @Override
    public int[] getItems() {
        return new int[] {OMEGA_BOOTS, OMEGA_BOOTS_32607};
    }
}
