package com.zenyte.plugins.drop.grotesqueguardians;

import com.zenyte.game.item.Item;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.drop.matrix.Drop;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor;
import com.zenyte.game.world.entity.player.Player;

import static com.zenyte.game.item.ids.ItemId.*;

/**
 * @author Tommeh | 04/08/2019 | 19:50
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class GrotesqueGuardiansProcessor extends DropProcessor {

    @Override
    public void attach() {
        //Granite gloves
        appendDrop(new DisplayedDrop(GRANITE_GLOVES, 1, 1, 100));
        //Granite ring
        appendDrop(new DisplayedDrop(GRANITE_RING, 1, 1, 100));
        //Granite maul
        appendDrop(new DisplayedDrop(GRANITE_MAUL, 1, 1, 125));
        //Granite hammer
        appendDrop(new DisplayedDrop(GRANITE_HAMMER, 1, 1, 200));
        //Dual cannonball mold
        appendDrop(new DisplayedDrop(DOUBLE_AMMO_MOULD, 1, 1, 300));
        //Black tourmaline core
        appendDrop(new DisplayedDrop(BLACK_TOURMALINE_CORE, 1, 1, 300));
        //Jar of stone
        appendDrop(new DisplayedDrop(JAR_OF_STONE, 1, 1, 600));
    }

    @Override
    public void onDeath(final NPC npc, final Player killer) {
        if (randomDrop(killer, 600) == 0) {
            npc.dropItem(killer, new Item(JAR_OF_STONE));
        }
    }

    @Override
    public Item drop(final NPC npc, final Player killer, final Drop drop, final Item item) {
        if (!drop.isAlways()) {
            if (randomDrop(killer, 300) == 0) {
                return new Item(DOUBLE_AMMO_MOULD);
            }
            if (randomDrop(killer, 300) == 0) {
                return new Item(BLACK_TOURMALINE_CORE);
            }
            if (randomDrop(killer, 200) == 0) {
                return new Item(GRANITE_HAMMER);
            }
            if (random(125) == 0) {
                return new Item(GRANITE_MAUL);
            }
            if (random(100) == 0) {
                return new Item(Utils.random(1) == 0 ? GRANITE_GLOVES : GRANITE_RING);
            }
        }
        return item;
    }

    @Override
    public int[] ids() {
        return new int[] { 7888 };
    }
}
