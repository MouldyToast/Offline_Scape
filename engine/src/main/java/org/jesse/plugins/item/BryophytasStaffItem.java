package org.jesse.plugins.item;

import org.jesse.game.item.Item;
import org.jesse.game.model.item.pluginextensions.ChargeExtension;
import org.jesse.game.model.item.pluginextensions.ItemPlugin;
import org.jesse.game.util.Colour;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.container.ContainerWrapper;
import org.jesse.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 09/10/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class BryophytasStaffItem extends ItemPlugin implements ChargeExtension {
    @Override
    public void handle() {
        bind("Uncharge", (player, item, container, slotId) -> {
            player.getDialogueManager().start(new Dialogue(player) {
                @Override
                public void buildDialogue() {
                    plain("Are you sure you'd like to uncharge the staff? The runes within will " + Colour.RED.wrap("not") + " be returned to you.");
                    options("Uncharge the staff?", new DialogueOption("Yes.", () -> {
                        if (player.getInventory().getItem(slotId) != item) {
                            return;
                        }
                        player.getInventory().set(slotId, new Item(22368));
                    }), new DialogueOption("No."));
                }
            });
        });
    }

    @Override
    public int[] getItems() {
        return new int[] {
                22368, 22370
        };
    }

    @Override
    public void removeCharges(Player player, Item item, ContainerWrapper wrapper, int slotId, int amount) {
        item.setCharges(Math.max(0, item.getCharges() - amount));
        if (item.getCharges() <= 0) {
            item.setId(22368);
            wrapper.refresh(slotId);
        }
    }

    @Override
    public void checkCharges(final Player player, final Item item) {
        player.sendMessage("Your Bryophyta's staff currently contains " + item.getCharges() + " charges.");
    }
}
