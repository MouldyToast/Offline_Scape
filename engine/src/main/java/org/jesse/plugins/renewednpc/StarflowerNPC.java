package org.jesse.plugins.renewednpc;

import org.jesse.game.item.Item;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.npc.ids.NpcId;
import org.jesse.game.world.entity.npc.actions.NPCPlugin;
import org.jesse.game.world.entity.player.Player;

public class StarflowerNPC extends NPCPlugin {

    @Override
    public void handle() {
        bind("Pick", new OptionHandler() {

            @Override
            public void handle(Player player, NPC npc) {
                if (!player.getInventory().hasFreeSlots()) {
                    player.sendMessage("You need some inventory space to pick any more starflowers.");
                    return;
                }
                npc.sendDeath();
                player.getInventory().addOrDrop(new Item(9017));
            }

            @Override
            public void execute(final Player player, final NPC npc) {
                player.stopAll();
                player.setFaceEntity(npc);
                handle(player, npc);
            }
        });
    }

    @Override
    public int[] getNPCs() {
        return new int[] { NpcId.STARFLOWER_1857 };
    }
}
