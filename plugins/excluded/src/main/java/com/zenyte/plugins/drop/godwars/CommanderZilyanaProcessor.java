package com.zenyte.plugins.drop.godwars;

import com.zenyte.game.item.Item;
import com.zenyte.game.world.entity.npc.NPC;
import com.zenyte.game.world.entity.npc.drop.matrix.Drop;
import com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor;
import com.zenyte.game.world.entity.player.Player;

import static com.near_reality.game.item.CustomItemId.PRIMAL_LONGSWORD;

/**
 * @author Tommeh | 25-11-2018 | 17:09
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class CommanderZilyanaProcessor extends DropProcessor {

    @Override
    public void attach() {
        //Saradomin sword
        appendDrop(new DisplayedDrop(11838, 1, 1, 38));
        //Saradomin's light
        appendDrop(new DisplayedDrop(13256, 1, 1, 50));
        //Armadyl crossbow
        appendDrop(new DisplayedDrop(11785, 1, 1, 100));
        //Saradomin hilt
        appendDrop(new DisplayedDrop(11814, 1, 1, 100));
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
            if (randomDrop(killer, 50) == 0) {
                return new Item(13256);
            }
            if (randomDrop(killer, 38) == 0) {
                return new Item(11838);
            }
            if ((random = randomDrop(killer, 200)) < 2) {
                return new Item(random == 0 ? 11785 : 11814);
            }
            if ((random = randomDrop(killer,150)) < 3) {
                return new Item(11818 + (random * 2));
            }
            if (randomDrop(killer, 750) == 0) {
                return new Item(PRIMAL_LONGSWORD);
            }
        }
        return item;
    }

    @Override
    public int[] ids() {
        return new int[] { 2205, 6493 };
    }
}
