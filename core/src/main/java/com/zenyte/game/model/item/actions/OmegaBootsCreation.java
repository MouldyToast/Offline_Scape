package com.zenyte.game.model.item.actions;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.ItemOnItemAction;
import com.zenyte.game.world.broadcasts.BroadcastType;
import com.zenyte.game.world.broadcasts.WorldBroadcasts;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.RequestResult;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

import java.util.List;

import static com.zenyte.game.item.ItemId.*;
import static com.zenyte.game.item.ItemId.PEGASIAN_BOOTS;
import static com.zenyte.game.item.ItemId.PRIMORDIAL_BOOTS;

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2025-04-22
 */
public class OmegaBootsCreation implements ItemOnItemAction {

    @Override
    public int[] getItems() {
        return new int[] {
            PRIMORDIAL_BOOTS,
            ItemId.ETERNAL_BOOTS,
            PEGASIAN_BOOTS,
            OMEGA_HORN,
            OMEGA_SPIKE,
            OMEGA_SYMBOL
        };
    }

    @Override
    public void handleItemOnItemAction(Player player, Item from, Item to, int fromSlot, int toSlot) {
        if (!hasAllPieces(player)) {
            player.sendMessage("You are missing a piece required to make these boots.");
            return;
        }
        player.getDialogueManager().start(new Dialogue(player) {
            @Override
            public void buildDialogue() {
                doubleItem(OMEGA_BOOTS, OMEGA_BOOTS_32607,
                    "You are about to build the Omega boots.<br>>You can toggle the cosmetic visual of them through a menu option.");
                options("Are you sure you want to build the Omega boots?",
                    new DialogueOption("Yes, craft the boots.", () -> {
                        if (player.getInventory().deleteItems(getPiecesAsItems()).getResult() == RequestResult.SUCCESS) {
                            var boots = new Item(OMEGA_BOOTS, 1);
                            player.getInventory().addItem(boots);
                            player.getDialogueManager().item(boots, "Congratulations! You have successfully created the Omega boots.");
                            WorldBroadcasts.broadcast(player, BroadcastType.RARE_DROP, boots, "Crafting");
                        }
                    }),
                    new DialogueOption("Nevermind.", () -> {})
                );
            }
        });
    }

    private Item[] getPiecesAsItems() {
        return new Item[] {
            new Item(PRIMORDIAL_BOOTS, 1),
            new Item(ItemId.ETERNAL_BOOTS, 1),
            new Item(PEGASIAN_BOOTS, 1),
            new Item(OMEGA_HORN, 1),
            new Item(OMEGA_SPIKE, 1),
            new Item(OMEGA_SYMBOL, 1)
        };
    }

    private boolean hasAllPieces(Player player) {
        return player.getInventory().containsAll(
            List.of(
                PRIMORDIAL_BOOTS,
                ItemId.ETERNAL_BOOTS,
                PEGASIAN_BOOTS,
                OMEGA_HORN,
                OMEGA_SPIKE,
                OMEGA_SYMBOL
            )
        );
    }
}
