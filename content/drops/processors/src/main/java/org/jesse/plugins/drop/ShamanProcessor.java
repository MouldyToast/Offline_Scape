package org.jesse.plugins.drop;

import org.jesse.game.item.Item;
import org.jesse.game.item.ids.ItemId;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.npc.drop.matrix.Drop;
import org.jesse.game.world.entity.npc.drop.matrix.DropProcessor;
import org.jesse.game.world.entity.player.Player;

/**
 * @author Kris | 24/11/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ShamanProcessor extends DropProcessor {
    @Override
    public void attach() {
        appendDrop(new DisplayedDrop(ItemId.SHAMAN_MASK, 1, 1, 900));
    }

    @Override
    public Item drop(final NPC npc, final Player killer, final Drop drop, final Item item) {
        if (!drop.isAlways()) {
            if (random(900) == 0) {
                return new Item(ItemId.SHAMAN_MASK);
            }
        }
        return item;
    }

    @Override
    public int[] ids() {
        return new int[] {
                7989, 7990, 7991, 7992
        };
    }
}
