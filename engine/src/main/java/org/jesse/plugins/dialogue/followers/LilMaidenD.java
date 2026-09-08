package org.jesse.plugins.dialogue.followers;

import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.dialogue.Dialogue;

import java.util.Random;

/**
 * @author Leviticus | 04-21-2025 | 12:54
 * @see <a href="https://rune-server.org/members/leviticus.180707/">Rune-Server profile</a>}
 */
public class LilMaidenD extends Dialogue {

    public LilMaidenD(final Player player, final NPC npc) {
        super(player, npc);
    }

    @Override
    public void buildDialogue() {
        final int random = new Random().nextInt(0, 2);
        switch (random) {
            case 0:
                player("Have you seen my brother?");
                npc("He was a kind boy, with a bright smile...");
                player("I'm not sure, what did he look like?");
                npc("Oh... I'd very much like to see him again.");
                break;
            case 1:
                npc("Thank you for freeing me...");
                player("Freeing you? I didn't know you were trapped.");
                npc("The vampyres... they tricked me...");
                player("Oh, you're welcome then!");
                break;
            default:
                // fallback
                player("Hello, Lil' Maiden.");
                npc("...");
                break;
        }
    }
}
