package org.jesse.game.content.trouver;

import org.jesse.game.item.Item;
import org.jesse.game.model.item.ItemOnNPCAction;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.dialogue.Dialogue;
import mgi.types.config.items.ItemDefinitions;

public class TrouvesUseItemPlugin implements ItemOnNPCAction {

    @Override
    public void handleItemOnNPCAction(Player player, Item item, int slot, NPC npc) {
        player.getDialogueManager().start(new Dialogue(player, npc) {
            @Override
            public void buildDialogue() {
                npc("I can lock that item so that it won't vanish if you die in depp wilderness. " +
                        "It'll cost you 500,000 coins and a Trouver parchment");
                options("Select an Option",
                        "Lock item (" + ItemDefinitions.get(item.getId()).getName() + ")",
                        "Lock all available items",
                        "Cancel")
                        .onOptionOne(() -> {
                            TrouverData.protectItem(player, item, slot);
                        })
                        .onOptionThree(() -> finish());
            }
        });
    }


    @Override
    public Object[] getItems() {
        return TrouverData.ORIGINAL_ITEMS.toArray();
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { 7456 };
    }
}
