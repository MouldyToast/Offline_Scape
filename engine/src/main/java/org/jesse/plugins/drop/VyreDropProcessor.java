package org.jesse.plugins.drop;

import org.jesse.game.item.Item;
import org.jesse.game.item.ids.ItemId;
import org.jesse.game.util.Utils;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.npc.drop.matrix.Drop;
import org.jesse.game.world.entity.npc.drop.matrix.DropProcessor;
import org.jesse.game.world.entity.player.Player;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.runelite.api.ItemID;

public class VyreDropProcessor extends DropProcessor {

    @Override
    public void attach() {
        appendDrop(new DisplayedDrop(ItemId.BLOOD_TALISMAN, 1, 1, 200));
    }

    @Override
    public Item drop(final NPC npc, final Player killer, final Drop drop, final Item item) {
        if(Utils.random(200) == 0) {
            npc.dropItem(killer, new Item(ItemID.BLOOD_TALISMAN, 1));
        }
        return item;
    }

    @Override
    public int[] ids() {
        int[] ids = new int[] {9727, 9734, 8326, 8327, 9590, 9591, 9735, 9742, 3137, 3234, 3237, 3239, 3707, 3708, 4431, 8678};

        int[][] idRanges = new int[][] {
                {3690, 3700},
                {4436, 4439},
                {9599, 9608},
                {9756, 9763},
                {11169, 11173},
                {3709, 3732},
                {3748, 3771},
                {8251, 8259},
                {8300, 8307},
                {5640, 5642},
        };

        ObjectArrayList<Integer> listOfIds = new ObjectArrayList<>();
        for (int[] idRange : idRanges) {
            for (int i = idRange[0]; i <= idRange[1]; i++) {
                listOfIds.add(i);
            }
        }
        for (Integer id : ids) {
            listOfIds.add(id);
        }
        return listOfIds.stream().mapToInt(i -> i).toArray();
    }
}
