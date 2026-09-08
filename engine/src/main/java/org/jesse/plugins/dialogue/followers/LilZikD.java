package org.jesse.plugins.dialogue.followers;

import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.dialogue.Dialogue;

import java.util.Random;

/**
 * @author Tommeh | 23-11-2018 | 23:33
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class LilZikD extends Dialogue {

    public LilZikD(final Player player, final NPC npc) {
        super(player, npc);
    }

    @Override
    public void buildDialogue() {
        final int random = new Random().nextInt(0, 3);
        switch (random) {
            case 0:
                player("Hey Lil' Zik.");
                npc("Stop.");
                npc("Calling.");
                npc("Me.");
                npc("Little.");
                player("Never!");
                break;
            case 1:
                player("You know... you're not like the other spiders.");
                npc("You know I hate it when you sa that... please leave me alone.");
                player("But I earned you fair and square at the Theatre of Blood! You're mine to keep.");
                npc("...");
                break;
            case 2:
                player("Incy wincy Verzik climbed up the water spout...");
                player("Down came the rain and washed poor Verzik out...");
                npc("Out came the Vampyre to put an end to this at once. Humans deserve only one fate!");
                player("Wow, calm down. It's just a nursery rhyme.");
                npc("I'm not in the mood.");
                break;
            default:
                player("Hi, I'm here for my reward!");
                npc("Not again...");
                break;
        }
    }
}
