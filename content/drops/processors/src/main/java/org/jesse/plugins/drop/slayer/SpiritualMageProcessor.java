package org.jesse.plugins.drop.slayer;

import org.jesse.game.item.Item;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.npc.drop.matrix.Drop;
import org.jesse.game.world.entity.npc.drop.matrix.DropProcessor;
import org.jesse.game.world.entity.player.Player;

/**
 * @author Tommeh | 25-11-2018 | 19:34
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class SpiritualMageProcessor extends DropProcessor {

    @Override
    public void attach() {
        //Dragon boots
        appendDrop(new DisplayedDrop(11840, 1, 1, 64));
    }

    @Override
    public Item drop(final NPC npc, final Player killer, final Drop drop, final Item item) {
        if (!drop.isAlways()) {
            if (random(64) == 0) {
                return new Item(11840);
            }
        }
        return item;
    }

    @Override
    public int[] ids() {
        return new int[] { 2212, 2244, 3161, 3168 };
    }
}
