package org.jesse.plugins.drop.dragons;

import org.jesse.game.item.Item;
import org.jesse.game.item.ids.ItemId;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.npc.ids.NpcId;
import org.jesse.game.world.entity.npc.drop.matrix.DropProcessor;
import org.jesse.game.world.entity.player.Player;

public class BasiliskKnightProcessor extends DropProcessor {
    @Override
    public void attach() {
        appendDrop(new DisplayedDrop(ItemId.BASILISK_JAW, 1, 1, 500));
    }

    @Override
    public void onDeath(NPC npc, Player killer) {
        if(randomDrop(killer, 500) == 0) {
            npc.dropItem(killer, new Item(ItemId.BASILISK_JAW));
        }
    }

    @Override
    public int[] ids() {
        return new int[]{NpcId.BASILISK_KNIGHT};
    }
}
