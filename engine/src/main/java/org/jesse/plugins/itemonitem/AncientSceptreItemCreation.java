package org.jesse.plugins.itemonitem;

import org.jesse.game.item.Item;
import org.jesse.game.item.ids.ItemId;
import org.jesse.game.model.item.ItemOnItemAction;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.container.RequestResult;
import org.jesse.game.world.entity.player.dialogue.Dialogue;
import org.jesse.plugins.item.sceptre.AncientSceptre;

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-08-29
 */
public class AncientSceptreItemCreation extends AncientSceptre implements ItemOnItemAction {
    @Override
    public int[] getItems() {
        return new int[] { ItemId.ANCIENT_STAFF, ItemId.ANCIENT_ICON };
    }

    @Override
    public void handleItemOnItemAction(Player player, Item from, Item to, int fromSlot, int toSlot) {
        final var sceptreToReward = new Item(ItemId.ANCIENT_SCEPTRE);
        final var message = "You're about to combine the Ancient Icon and the Ancient Staff to make an Ancient Sceptre.";
        player.getDialogueManager().start(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                plain("<col=FF0040>Warning!</col><br>".concat(message));
                options("Are you sure you wish to do this?", "Yes.", "No.")
                    .onOptionOne(() -> {
                        if (player.getInventory().deleteItems(from, to).getResult() == RequestResult.SUCCESS)
                            player.getInventory().addItem(sceptreToReward);
                    });
            }
        });

    }
}
