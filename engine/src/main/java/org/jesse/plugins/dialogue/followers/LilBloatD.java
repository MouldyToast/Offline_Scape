package org.jesse.plugins.dialogue.followers;

import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.dialogue.Dialogue;

import java.util.Random;

/**
 * @author Leviticus | 04-21-2025 | 12:54
 * @see <a href="https://rune-server.org/members/leviticus.180707/">Rune-Server profile</a>}
 */
public class LilBloatD extends Dialogue {

    public LilBloatD(final Player player, final NPC npc) {
        super(player, npc);
    }

    @Override
    public void buildDialogue() {
        final int random = new Random().nextInt(0, 3);
        switch (random) {
            case 0:
                npc("Maaasteeer?");
                player("I suppose I am your master now, I hadn't thought about it.");
                npc("Kill maasteer...");
                player("No, don't do that!");
                break;
            case 1:
                player("Do you smell that?");
                npc("Hnrgh?");
                player("*Sniffs* What are you made of Lil' Bloat?");
                npc("Cccorpsssee...");
                player("That would explain the smell...");
                break;
            case 2:
                player("Hello Lil' Bloat.");
                npc("...Hhhh-.");
                player("Are you trying to talk?");
                npc("...Hhhhuu-");
                player("Are you trying to say Hello? Come on you can do it!");
                npc("...Hhhuun-\n...Hhuunnggrryy.");
                player("Oh, of course...");
                break;
            default:
                player("Hi there, Lil' Bloat.");
                npc("...");
                break;
        }
    }
}
