package org.jesse.plugins.dialogue.followers;

import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.dialogue.Dialogue;

import java.util.Random;

/**
 * @author Leviticus | 04-21-2025 | 12:54
 * @see <a href="https://rune-server.org/members/leviticus.180707/">Rune-Server profile</a>}
 */
public class LilSotD extends Dialogue {

    public LilSotD(final Player player, final NPC npc) {
        super(player, npc);
    }

    @Override
    public void buildDialogue() {
        final int random = new Random().nextInt(0, 2);
        switch (random) {
            case 0:
                player("Hello my angry little monster.");
                npc("Grrrrruff!");
                player("You don't mean that, you're just grumpy.");
                npc("Grrrr...");
                player("That's a good girl.");
                break;
            case 1:
                player("Would you like a treat?");
                npc("Grruff?");
                player("Oh, you don't eat normal food, I'm afraid I don't have any adventurers to feed you.");
                npc("Grrrrrr...");
                player("I'm sorry! Please don't send me to the shadow realm!");
                break;
            default:
                player("Hey Lil' Sot.");
                npc("...");
                break;
        }
    }
}
