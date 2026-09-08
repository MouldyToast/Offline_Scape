package org.jesse.plugins.renewednpc;

import org.jesse.game.world.entity.npc.actions.NPCPlugin;
import org.jesse.plugins.dialogue.EstateAgentD;

/**
 * @author Kris | 26/11/2018 18:44
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class EstateAgent extends NPCPlugin {
    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> player.getDialogueManager().start(new EstateAgentD(player, npc)));
    }

    @Override
    public int[] getNPCs() {
        return new int[] {5419};
    }
}
