package org.jesse.game.content.treasuretrails.plugins;

import org.jesse.game.content.treasuretrails.TreasureTrail;
import org.jesse.game.item.Item;
import org.jesse.game.item.ids.ItemId;
import org.jesse.game.npc.ids.NpcId;
import org.jesse.game.world.entity.npc.actions.NPCPlugin;
import org.jesse.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 19/04/2019 22:44
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ReldoNPC extends NPCPlugin {

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> {
            if (TreasureTrail.withoutHotColdClue(player, true)) {
                player.getDialogueManager().start(new Dialogue(player, npc) {

                    @Override
                    public void buildDialogue() {
                        npc("Good evening.");
                    }
                });
            } else {
                player.getDialogueManager().start(new Dialogue(player, npc) {

                    @Override
                    public void buildDialogue() {
                        if (player.containsItem(ItemId.STRANGE_DEVICE_23183)) {
                            npc("I've already given you a strange device. Use it to find the position where the treasure is buried.");
                            return;
                        }
                        npc("Ah, I see you have a clue scroll that requires my assistance.").executeAction(() -> player.getInventory().addOrDrop(new Item(ItemId.STRANGE_DEVICE_23183)));
                        item(new Item(ItemId.STRANGE_DEVICE_23183), "Reldo hands you a strange device.");
                        npc("You can use the device to locate the position where the treasure is buried.");
                    }
                });
            }
        });
    }

    @Override
    public int[] getNPCs() {
        return new int[] { 6203, NpcId.RELDO_4243, NpcId.RELDO };
    }
}
