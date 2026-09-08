package org.jesse.plugins.renewednpc;

import org.jesse.game.GameInterface;
import org.jesse.game.npc.ids.NpcId;
import org.jesse.game.world.entity.npc.actions.NPCPlugin;

public class Asyff extends NPCPlugin {
    @Override
    public void handle() {
        bind("Fur clothing", (player, npc) -> {
            GameInterface.CUSTOM_FUR_CLOTHING.open(player);
        });
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.ASYFF }; // 2887
    }
}
