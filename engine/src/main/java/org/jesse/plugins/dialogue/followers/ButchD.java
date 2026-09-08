package org.jesse.plugins.dialogue.followers;

import org.jesse.game.util.Utils;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.dialogue.Dialogue;

/**
 * Dialogue implementation for the Butch pet.
 * Follows the exact in-game transcript.
 *
 * @author Your Name | 02-19-2025 | 14:00
 */
public class ButchD extends Dialogue {

    public ButchD(final Player player, final NPC npc) {
        super(player, npc);
    }

    @Override
    public void buildDialogue() {
        int choice = Utils.random(2);
        if (choice == 0) {
            player("How's it going, Butch?");
            npc("...");
            player("Ah... How could I forget... You don't have a head!");
            npc("...");
            player("Shall we head off on an adventure together?");
            player("Sorry, poor choice of words.");
            npc("...");
        } else {
            player("Can you speak?");
            npc("Mysterious Voice: Yes...");
            player("Er... Who's speaking? Is this Butch, or something else?");
            npc("Mysterious Voice: Yes...");
            player("That's unsettling.");
        }
    }
}
