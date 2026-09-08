package org.jesse.plugins.drop.slayer;

import org.jesse.game.item.Item;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.npc.drop.matrix.Drop;
import org.jesse.game.world.entity.npc.drop.matrix.DropProcessor;
import org.jesse.game.world.entity.player.Player;

/**
 * @author Tommeh | 25-11-2018 | 19:20
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
@SuppressWarnings("unused")
public class SmokeDevilProcessor extends DropProcessor {

    @Override
    public void attach() {
        //Occult necklace
        appendDrop(new DisplayedDrop(12002, 1, 1, 240));
        //Dragon chainbody
        appendDrop(new DisplayedDrop(3140, 1, 1, 32768));
    }

    @Override
    public Item drop(final NPC npc, final Player killer, final Drop drop, final Item item) {
        if (!drop.isAlways()) {
            if (random(32768) == 0) {
                return new Item(3140);
            }
            if (random(240) == 0) {
                return new Item(12002);
            }
        }
        return item;
    }

    @Override
    public int[] ids() {
        return new int[] { 498 };
    }
}
