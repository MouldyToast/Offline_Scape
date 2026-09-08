package org.jesse.game.content.boss.wildernessbosses.callisto;

import org.jesse.game.item.Item;
import org.jesse.game.item.ids.ItemId;
import org.jesse.game.util.Utils;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.npc.ids.NpcId;
import org.jesse.game.world.entity.npc.drop.matrix.Drop;
import org.jesse.game.world.entity.npc.drop.matrix.DropProcessor;
import org.jesse.game.world.entity.player.Player;

/**
 * @author Andys1814
 */
public final class CallistoDropProcessor extends DropProcessor {


    @Override
    public void attach() {
        appendDrop(new DisplayedDrop(27667, 1, 1, 137));
        appendDrop(new DisplayedDrop(ItemId.DRAGON_2H_SWORD, 1, 1, 179));
        appendDrop(new DisplayedDrop(ItemId.DRAGON_PICKAXE, 1, 1, 179));
        appendDrop(new DisplayedDrop(27681, 1, 1, 252));
        appendDrop(new DisplayedDrop(ItemId.TYRANNICAL_RING, 1, 1, 358));
    }

    @Override
    public Item drop(NPC npc, Player killer, Drop drop, Item item) {
        if (!drop.isAlways()) {
            if (random(137) == 0) {
                return new Item(27667);
            }
            if (random(179) == 0) {
                return new Item(Utils.roll(50) ? ItemId.DRAGON_2H_SWORD : ItemId.DRAGON_PICKAXE);
            }
            if (random(252) == 0) {
                return new Item(27681);
            }
            if (random(358) == 0) {
                return new Item(ItemId.TYRANNICAL_RING);
            }
        }
        return item;
    }

    @Override
    public int[] ids() {
        return new int[] {
            NpcId.CALLISTO_6609,
        };
    }

}
