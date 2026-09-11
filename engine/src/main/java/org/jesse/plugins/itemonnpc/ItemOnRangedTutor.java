package org.jesse.plugins.itemonnpc;

import org.jesse.game.item.Item;
import org.jesse.game.item.ids.ItemId;
import org.jesse.game.model.item.ItemOnNPCAction;
import org.jesse.game.npc.ids.NpcId;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.dialogue.Dialogue;

public class ItemOnRangedTutor implements ItemOnNPCAction {

    @Override
    public void handleItemOnNPCAction(Player player, Item item, int slot, NPC npc) {
        int itemId = item.getId();
        if (itemId == ItemId.TRAINING_BOW) {
            player.getDialogueManager().start(new Dialogue(player, npc) {
                @Override
                public void buildDialogue() {
                    player("I no longer need my Training bow, would you like it back?");
                    npc("Sure thing, I can give this to another adventurer who is in need of training.").executeAction(() ->
                            player.getInventory().deleteItem(new Item(ItemId.TRAINING_BOW)));
                    plain("You give back your training bow to the Ranged combat tutor.");
                }
            });
        } else if (itemId == ItemId.TRAINING_ARROWS) {
            player.getDialogueManager().start(new Dialogue(player, npc) {
                @Override
                public void buildDialogue() {
                    player("I no longer need my Training arrows, would you like it back?");
                    npc("Sure thing, I can give this to another adventurer who is in need of training.").executeAction(() ->
                            player.getInventory().deleteItem(new Item(ItemId.TRAINING_ARROWS, item.getAmount())));
                    plain("You give back your training arrows to the Ranged combat tutor.");
                }
            });
        } else {
            player.getDialogueManager().start(new Dialogue(player, npc) {
                @Override
                public void buildDialogue() {
                    npc("I have no use for that, sorry.");
                }
            });
        }
    }

    @Override
    public Object[] getItems() {
        return null;
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { NpcId.RANGED_COMBAT_TUTOR };
    }
}