package org.jesse.plugins.drop.slayer;

import org.jesse.game.item.Item;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.npc.drop.matrix.Drop;
import org.jesse.game.world.entity.npc.drop.matrix.DropProcessor;
import org.jesse.game.world.entity.player.Player;

/**
 * @author Kris | 21/04/2019 17:31
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class FireGiantProcessor extends DropProcessor {
    @Override
    public void attach() {
        appendDrop(new DisplayedDrop(1333, 1, 1, 128));
    }

    @Override
    public Item drop(final NPC npc, final Player killer, final Drop drop, final Item item) {
        if (!drop.isAlways()) {
            if (random(128) == 0) {
                return new Item(1333);
            }
        }
        return super.drop(npc, killer, drop, item);
    }

    @Override
    public int[] ids() {
        return new int[] {
                2075, 2076, 2077, 2078, 2079, 2080, 2081, 2082, 2083, 2084, 7251, 7252
        };
    }
}
