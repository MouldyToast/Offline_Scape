package com.zenyte.plugins.drop.godwars;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.drop.matrix.Drop;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor;
import com.zenyte.game.world.entity.player.Player;

import static com.near_reality.game.item.CustomItemId.PRIMAL_LONGSWORD;

/**
 * @author Tommeh | 25-11-2018 | 16:49
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class KrilTsutsarothProcessor extends DropProcessor {

    @Override
    public void attach() {
        //Steam battlestaff
        appendDrop(new DisplayedDrop(11787, 1, 1, 50));
        //Zamorakian spear
        appendDrop(new DisplayedDrop(11824, 1, 1, 50));
        //Staff of the dead
        appendDrop(new DisplayedDrop(11791, 1, 1, 75));
        //Zamorak hilt
        appendDrop(new DisplayedDrop(11816, 1, 1, 100));
        //Godsword shards
        appendDrop(new DisplayedDrop(11818, 1, 1, 50));
        appendDrop(new DisplayedDrop(11820, 1, 1, 50));
        appendDrop(new DisplayedDrop(11822, 1, 1, 50));
        appendDrop(new DisplayedDrop(PRIMAL_LONGSWORD, 1, 1, 750));
    }

    @Override
    public Item drop(final NPC npc, final Player killer, final Drop drop, final Item item) {
        if (!drop.isAlways()) {
            int random;
            if ((random = random(100)) < 2) {
                return new Item(random == 0 ? 11787 : 11824);
            }
            if ((random = random(150)) < 3) {
                return new Item(11818 + (random * 2));
            }
            if(randomDrop(killer, 100) == 0) {
                return new Item(ItemId.ZAMORAK_HILT);
            }
            if(randomDrop(killer, 75) == 0) {
                return new Item(ItemId.STAFF_OF_THE_DEAD);
            }
            if (randomDrop(killer, 750) == 0) {
                return new Item(PRIMAL_LONGSWORD);
            }
        }
        return item;
    }

    @Override
    public int[] ids() {
        return new int[] { 3129, 6495 };
    }
}
