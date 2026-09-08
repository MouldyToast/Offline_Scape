package org.jesse.plugins.drop.godwars;

import org.jesse.game.item.Item;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.npc.drop.matrix.Drop;
import org.jesse.game.world.entity.npc.drop.matrix.DropProcessor;
import org.jesse.game.world.entity.player.Player;


/**
 * @author Kris | 24/11/2018 21:13
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class GeneralGraardorProcessor extends DropProcessor {

    @Override
    public void attach() {
        //Armour
        appendDrop(new DisplayedDrop(11832, 1, 1, 75));
        appendDrop(new DisplayedDrop(11834, 1, 1, 75));
        appendDrop(new DisplayedDrop(11836, 1, 1, 75));
        //Bandos hilt
        appendDrop(new DisplayedDrop(11812, 1, 1, 100));
        //Godsword shards
        appendDrop(new DisplayedDrop(11818, 1, 1, 50));
        appendDrop(new DisplayedDrop(11820, 1, 1, 50));
        appendDrop(new DisplayedDrop(11822, 1, 1, 50));
        appendDrop(new DisplayedDrop(11822, 1, 1, 50));
    }

    @Override
    public Item drop(final NPC npc, final Player killer, final Drop drop, final Item item) {
        if (!drop.isAlways()) {
            int itemId;
            if ((itemId = rollPool(killer, 50, 11818, 11820, 11822)) != -1) {
                return new Item(itemId);
            }
            if ((itemId = rollPool(killer, 75, 11832, 11834, 11836)) != -1) {
                return new Item(itemId);
            }
            if (randomDrop(killer, 100) == 0) {
                return new Item(11812);
            }
        }
        return item;
    }

    @Override
    public int[] ids() {
        return new int[] { 2215, 6494 };
    }
}
