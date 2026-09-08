package org.jesse.plugins.item;

import org.jesse.game.item.Item;
import org.jesse.game.model.item.pluginextensions.ItemPlugin;
import org.jesse.plugins.dialogue.ItemChat;

import static org.jesse.game.item.ids.ItemId.BASILISK_JAW;

public class BasiliskJaw extends ItemPlugin {
    @Override
    public void handle() {
        bind("Inspect", (player, item, slotId) ->
                player.getDialogueManager().start(
                        new ItemChat(player, new Item(BASILISK_JAW), "It's the jaw of a Basilisk Knight. It looks like it could be attached to a Helm of Neitiznot.")));
    }

    @Override
    public int[] getItems() {
        return new int[] {BASILISK_JAW};
    }
}
