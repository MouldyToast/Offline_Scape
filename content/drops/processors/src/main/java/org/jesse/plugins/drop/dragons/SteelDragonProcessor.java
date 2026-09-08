package org.jesse.plugins.drop.dragons;

import org.jesse.game.item.Item;
import org.jesse.game.util.Utils;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.npc.drop.matrix.Drop;
import org.jesse.game.world.entity.npc.drop.matrix.DropProcessor;
import org.jesse.game.world.entity.player.Player;

/**
 * @author Kris | 21/04/2019 17:51
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class SteelDragonProcessor extends DropProcessor {
    @Override
    public void attach() {
        appendDrop(new DisplayedDrop(4087, 1, 1, 512));
        appendDrop(new DisplayedDrop(4585, 1, 1, 512));
    }

    @Override
    public Item drop(final NPC npc, final Player killer, final Drop drop, final Item item) {
        if (!drop.isAlways()) {
            if (random(256) == 0) {
                return new Item(Utils.random(1) == 0 ? 4585 : 4087);
            }
        }
        return super.drop(npc, killer, drop, item);
    }

    @Override
    public int[] ids() {
        return new int[] {
                139, 274, 275, 7255, 8086
        };
    }
}
