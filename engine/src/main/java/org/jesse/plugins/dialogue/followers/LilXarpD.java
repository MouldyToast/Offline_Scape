package org.jesse.plugins.dialogue.followers;

import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Leviticus | 04-21-2025 | 12:54
 * @see <a href="https://rune-server.org/members/leviticus.180707/">Rune-Server profile</a>}
 */
public class LilXarpD extends Dialogue {

    public LilXarpD(final Player player, final NPC npc) {
        super(player, npc);
    }

    @Override
    public void buildDialogue() {
        player("We're not in the Theatre anymore, so no poisoning anyone, got that?");
        npc("Scraaa?");
        player("I mean it, no spitting poison at people or on the floor.");
        npc("Scraaa....");
    }
}
