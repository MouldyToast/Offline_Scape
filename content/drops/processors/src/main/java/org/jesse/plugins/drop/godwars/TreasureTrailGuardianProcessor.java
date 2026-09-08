package org.jesse.plugins.drop.godwars;

import org.jesse.game.item.Item;
import org.jesse.game.util.Utils;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.npc.drop.matrix.Drop;
import org.jesse.game.world.entity.npc.drop.matrix.DropProcessor;
import org.jesse.game.world.entity.player.Player;

/**
 * @author Kris | 12/04/2019 17:48
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class TreasureTrailGuardianProcessor extends DropProcessor {
    @Override
    public void attach() {
        appendDrop(new DisplayedDrop(11826, 1, 1, 1000000));
        appendDrop(new DisplayedDrop(11836, 1, 1, 1000000));
    }

    @Override
    public Item drop(final NPC npc, final Player killer, final Drop drop, final Item item) {
        if (!drop.isAlways()) {
            if (random(1000000) == 0) {
                return new Item(Utils.random(1) == 0 ? 11826 : 11836);
            }
        }
        return item;
    }


    @Override
    public int[] ids() {
        return new int[] {
                6587, 6588
        };
    }
}
