package org.jesse.game.content.chambersofxeric.plugins.npc;

import org.jesse.game.content.chambersofxeric.npc.MeatTree;
import org.jesse.game.content.chambersofxeric.room.MuttadileRoom;
import org.jesse.game.world.entity.npc.actions.NPCPlugin;

/**
 * @author Kris | 06/07/2019 17:12
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class MeatTreePlugin extends NPCPlugin {

    @Override
    public void handle() {
        bind("Cut", (player, npc) -> player.getRaid().ifPresent(raid -> player.getActionManager().setAction(new MuttadileRoom.MeatTreeWoodcutting(npc, raid))));
    }

    @Override
    public int[] getNPCs() {
        return new int[] {
                MeatTree.MEAT_TREE
        };
    }
}
