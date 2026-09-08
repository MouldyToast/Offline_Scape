package org.jesse.plugins.renewednpc;

import org.jesse.game.content.treasuretrails.TreasureTrail;
import org.jesse.game.npc.ids.NpcId;
import org.jesse.game.world.entity.npc.actions.NPCPlugin;
import org.jesse.plugins.dialogue.HairdresserD;

/**
 * @author Kris | 26/11/2018 18:25
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class Hairdresser extends NPCPlugin {

    @Override
    public void handle() {
        bind("Talk-to", (player, npc) -> {
            if (TreasureTrail.talk(player, npc)) {
                return;
            }
            player.getDialogueManager().start(new HairdresserD(player, npc, true));
        });
        bind("Haircut", (player, npc) -> player.getDialogueManager().start(new HairdresserD(player, npc, false)));
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.HAIRDRESSER };
    }
}
