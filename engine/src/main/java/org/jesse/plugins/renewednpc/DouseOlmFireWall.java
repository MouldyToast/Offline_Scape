package org.jesse.plugins.renewednpc;

import org.jesse.game.content.chambersofxeric.greatolm.GreatOlm;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.npc.ids.NpcId;
import org.jesse.game.world.entity.npc.actions.NPCPlugin;
import org.jesse.game.world.entity.player.Player;

/**
 * @author Christopher
 * @since 3/18/2020
 */
public class DouseOlmFireWall extends NPCPlugin {
    @Override
    public void handle() {
        bind("Douse", new OptionHandler() {
            @Override
            public void handle(Player player, NPC npc) {
                GreatOlm.douseFirewall(player, npc);
            }

            @Override
            public void click(final Player player, final NPC npc, final NPCOption option) {
                execute(player, npc);
            }
        });
    }

    @Override
    public int[] getNPCs() {
        return new int[]{NpcId.FIRE};
    }
}
