package org.jesse.plugins.renewednpc;

import org.jesse.game.content.treasuretrails.TreasureTrail;
import org.jesse.game.npc.ids.NpcId;
import org.jesse.game.world.entity.npc.actions.NPCPlugin;
import org.jesse.plugins.dialogue.lumbridge.CookD;

/**
 * @author Kris | 26/11/2018 20:04
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class Cook extends NPCPlugin {

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> {
            if (TreasureTrail.talk(player, npc)) {
                return;
            }
            player.getDialogueManager().start(new CookD(player, npc));
        });
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.COOK_4626 };
    }
}
