package org.jesse.plugins.renewednpc;

import org.jesse.game.content.area.taverley.KeyMasterD;
import org.jesse.game.content.treasuretrails.TreasureTrail;
import org.jesse.game.npc.ids.NpcId;
import org.jesse.game.world.entity.npc.actions.NPCPlugin;

/**
 * @author Kris | 26/11/2018 18:17
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class KeyMaster extends NPCPlugin {

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> {
            if (TreasureTrail.talk(player, npc)) {
                return;
            }
            player.getDialogueManager().start(new KeyMasterD(player, npc));
        });
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.KEY_MASTER };
    }
}
