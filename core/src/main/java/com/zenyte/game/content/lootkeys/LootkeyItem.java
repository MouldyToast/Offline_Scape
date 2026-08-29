package com.zenyte.game.content.lootkeys;

import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.masks.UpdateFlag;
import com.zenyte.plugins.dialogue.DestroyItemDialogue;

import java.util.Arrays;

public class LootkeyItem extends ItemPlugin {

    @Override
    public void handle() {
        bind("Check", ((player, item, container, slotId) -> {
            var index = Arrays.binarySearch(LootkeyConstants.LOOT_KEY_ORDER, item.getId());
            if (index < 0) return;

            var settings = player.getLootkeySettings();
            if (settings == null) return;

            var keyContainer = settings.getContainer(index);
            if (keyContainer == null) return;

            var itemValue = keyContainer.calculateValue();
            player.sendMessage("Your loot key contains items that are worth approximately "
                    + Utils.formatNumberWithCommas(itemValue) + "gp. ");

        }));
        bind("Destroy", ((player, item, container, slotId) -> {
            player.getDialogueManager().start(
                    new DestroyItemDialogue(player, item, slotId).setOnCloseRunnable(() -> player.getUpdateFlags().flag(UpdateFlag.APPEARANCE))
            );

        }));
    }

    @Override
    public int[] getItems() {
        return LootkeyConstants.LOOT_KEY_ORDER;
    }
}
