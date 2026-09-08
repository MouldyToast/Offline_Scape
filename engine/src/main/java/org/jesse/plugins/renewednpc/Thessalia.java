package org.jesse.plugins.renewednpc;

import org.jesse.game.world.entity.npc.actions.NPCPlugin;
import org.jesse.plugins.dialogue.varrock.ThessaliaD;

/**
 * @author Kris | 25/11/2018 16:24
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class Thessalia extends NPCPlugin {
    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> player.getDialogueManager().start(new ThessaliaD(player, npc, true)));
        bind("Makeover", (player, npc) -> player.getDialogueManager().start(new ThessaliaD(player, npc, false)));
    }

    @Override
    public int[] getNPCs() {
        return new int[] {534};
    }
}
