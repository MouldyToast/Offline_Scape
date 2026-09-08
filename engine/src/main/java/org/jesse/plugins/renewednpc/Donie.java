package org.jesse.plugins.renewednpc;

import org.jesse.game.npc.ids.NpcId;
import org.jesse.game.world.entity.npc.actions.NPCPlugin;
import org.jesse.plugins.dialogue.lumbridge.DonieD;

/**
 * @author Kris | 26/11/2018 20:06
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class Donie extends NPCPlugin {

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> player.getDialogueManager().start(new DonieD(player, npc)));
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.DONIE };
    }
}
