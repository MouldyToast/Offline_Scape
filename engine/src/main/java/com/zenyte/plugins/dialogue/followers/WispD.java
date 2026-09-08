package com.zenyte.plugins.dialogue.followers;

import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

public class WispD extends Dialogue {

    public WispD(final Player player, final NPC npc) {
        super(player, npc);
    }

    @Override
    public void buildDialogue() {
        int location = player.getLocation().getRegionId();

        if (location == 12345) { // Fortress below Trollweiss (
            player("How's it going, Wisp?");
            npc("The darkness here is comforting.");
            player("It is, isn't it.");
            npc("One day I will return to the shadows.");
            player("Will you now...?");
        } else if (location == 9571) { // Shadow Realm within Lassar Undercity
            player("How's it going, Wisp?");
            npc("I can feel the power of the shadows. You shall be my servant soon enough.");
            player("Well... that's dark.");
        } else {
            int choice = Utils.random(3);
            switch (choice) {
                case 0 -> {
                    player("I don't trust shadows.");
                    player("They act pretty shady...");
                    npc("Wow.");
                    npc("You can trust me, " + player.getName() + ".");
                    npc("Now, come closer... I have something to tell you...");
                    npc("*Wisp starts to whisper into your ear...*");
                    player("Woah, woah, woah! None of that! I know what your whispers are capable of.");
                }
                case 1 -> {
                    player("Can you sing me a song?");
                    npc("If I were to do that, your mind would perish.");
                    player("Maybe I can sing you a song instead?");
                    npc("I'd rather you d-");
                    player("Ehem...");
                    player("Let my voice lead you this waaay!");
                    player("I will not lead you astraaay!");
                    npc("Please... No more.");
                    player("Suit yourself. I'd make a great siren.");
                    npc("Mmmhmm.");
                }
                case 2 -> {
                    player("How's it going, Wisp?");
                    npc("There is too much light here. It's awful.");
                    player("I love it!");
                    npc("I miss the shadows.");
                }
            }
        }
    }
}
