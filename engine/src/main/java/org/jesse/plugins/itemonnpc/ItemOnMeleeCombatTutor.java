package org.jesse.plugins.itemonnpc;

import org.jesse.game.item.Item;
import org.jesse.game.item.ids.ItemId;
import org.jesse.game.model.item.ItemOnNPCAction;
import org.jesse.game.npc.ids.NpcId;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.dialogue.Dialogue;

public class ItemOnMeleeCombatTutor implements ItemOnNPCAction {

    @Override
    public void handleItemOnNPCAction(Player player, Item item, int slot, NPC npc) {
        int itemId = item.getId();
        if (itemId == ItemId.TRAINING_SWORD) {
            player.getDialogueManager().start(new Dialogue(player, npc) {
                @Override
                public void buildDialogue() {
                    player("I no longer need my Training sword, would you like it back?");
                    npc("Sure thing, I can give this to another adventurer who is in need of training.").executeAction(() ->
                            player.getInventory().deleteItem(new Item(ItemId.TRAINING_SWORD)));
                    plain("You give back your training sword to the Melee combat tutor.");
                }
            });
        } else if (itemId == ItemId.TRAINING_SHIELD) {
            player.getDialogueManager().start(new Dialogue(player, npc) {
                @Override
                public void buildDialogue() {
                    player("I no longer need my Training shield, would you like it back?");
                    npc("Sure thing, I can give this to another adventurer who is in need of training.").executeAction(() ->
                            player.getInventory().deleteItem(new Item(ItemId.TRAINING_SHIELD)));
                    plain("You give back your training shield to the Melee combat tutor.");
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
        return null; // null = any item
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { NpcId.MELEE_COMBAT_TUTOR };
    }
}