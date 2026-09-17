package org.jesse.plugins.item;

import org.jesse.game.item.ids.ItemId;
import org.jesse.game.model.item.pluginextensions.ItemPlugin;
import org.jesse.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 23/06/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ChinchompaItem extends ItemPlugin {
    @Override
    public void handle() {
    }

    @Override
    public void setDefaultHandlers() {
        super.setDefaultHandlers();
        setDefault("Drop", (player, item, slotId) -> {
            if (item.getAmount() == 1) {
                player.getInventory().deleteItem(slotId, item);
                player.sendFilteredMessage("You release the Chinchompa and it bounces away.");
            } else {
                player.getDialogueManager().start(new Dialogue(player) {
                    @Override
                    public void buildDialogue() {
                        options("Drop all of your " + item.getName().toLowerCase() + "s?",
                                new DialogueOption("Yes", () -> {
                                    if (player.getInventory().getItem(slotId) != item) {
                                        return;
                                    }
                                    player.getInventory().deleteItem(slotId, item);
                                    player.sendFilteredMessage("You release the Chinchompa and it bounces away.");
                                }), new DialogueOption("No"));
                    }
                });
            }
        });
    }

    @Override
    public int[] getItems() {
        return new int[] {
                ItemId.CHINCHOMPA_10033,
                ItemId.RED_CHINCHOMPA_10034,
                ItemId.BLACK_CHINCHOMPA
        };
    }
}
