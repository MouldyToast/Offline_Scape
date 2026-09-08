package org.jesse.plugins.dialogue.followers;

import org.jesse.game.util.Utils;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.dialogue.Dialogue;

public class LilViathanD extends Dialogue {

    public LilViathanD(final Player player, final NPC npc) {
        super(player, npc);
    }

    @Override
    public void buildDialogue() {
        int choice = Utils.random(3);
        switch (choice) {
            case 0 -> {
                player("How's it going, Lil'viathan?");
                npc("The abyss is endless. I am but a fragment of its will.");
                player("That sounds... ominous.");
                npc("The tides of fate pull all things into darkness.");
                player("Alright, well, stay positive!");
            }
            case 1 -> {
                player("You're a strange little creature.");
                npc("The Leviathan sees all.");
                player("Yeah, but you're not The Leviathan.");
                npc("I am its echo. A whisper of the abyss.");
                player("Creepy.");
            }
            case 2 -> {
                player("You look kinda cute.");
                npc("The abyss does not concern itself with appearances.");
                player("I think it should, you'd be very marketable.");
                npc("Your mortal concerns are insignificant.");
                player("Alright, geez. Just take the compliment.");
            }
        }
    }
}
