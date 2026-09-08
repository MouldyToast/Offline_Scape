package org.jesse.plugins.renewednpc;

import org.jesse.game.npc.ids.NpcId;
import org.jesse.game.world.entity.npc.actions.NPCPlugin;
import org.jesse.plugins.dialogue.magebank.ChamberGuardianD;

/**
 * @author Kris | 26/11/2018 19:32
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ChamberGuardian extends NPCPlugin {

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> player.getDialogueManager().start(new ChamberGuardianD(player, npc)));
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.CHAMBER_GUARDIAN };
    }
}
