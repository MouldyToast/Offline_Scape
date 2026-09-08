package org.jesse.plugins.dialogue.followers;

import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Leviticus | 04-21-2025 | 12:54
 * @see <a href="https://rune-server.org/members/leviticus.180707/">Rune-Server profile</a>}
 */
public class LilNyloD extends Dialogue {

    public LilNyloD(final Player player, final NPC npc) {
        super(player, npc);
    }

    @Override
    public void buildDialogue() {
        player("How are you doing little guy?");
        npc("Chitter-chitter?");
        player("You seem happy to be free of the Theatre.");
        npc("Chitter-chitter!");
    }
}
