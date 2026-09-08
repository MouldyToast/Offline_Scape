package org.jesse.plugins.drop.krakens;

import org.jesse.game.item.Item;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.npc.drop.matrix.Drop;
import org.jesse.game.world.entity.npc.drop.matrix.DropProcessor;
import org.jesse.game.world.entity.player.Player;

/**
 * @author Tommeh | 25-11-2018 | 17:43
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
@SuppressWarnings("unused")
public class KrakenProcessor extends DropProcessor {

    @Override
    public void attach() {
        //Kraken tentacle
        appendDrop(new DisplayedDrop(12004, 1, 1, 125));
        //Trident of the seas (full)
        appendDrop(new DisplayedDrop(11905, 1, 1, 175));
        //Jar of dirt
        appendDrop(new DisplayedDrop(12007, 1, 1, 600));
    }

    @Override
    public void onDeath(final NPC npc, final Player killer) {
        if (randomDrop(killer,600) == 0) {
            npc.dropItem(killer, new Item(12007));
        }
    }

    @Override
    public Item drop(final NPC npc, final Player killer, final Drop drop, final Item item) {
        if (!drop.isAlways()) {
            if (randomDrop(killer,175) == 0) {
                return new Item(11905, 1);
            }
            if (randomDrop(killer,125) == 0) {
                return new Item(12004);
            }
        }
        return item;
    }

    @Override
    public int[] ids() {
        return new int[] { 494, 496 };
    }
}
