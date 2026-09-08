package org.jesse.plugins.drop.slayer;

import org.jesse.game.content.treasuretrails.clues.SherlockTask;
import org.jesse.game.item.Item;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.npc.drop.matrix.Drop;
import org.jesse.game.world.entity.npc.drop.matrix.DropProcessor;
import org.jesse.game.world.entity.player.Player;

/**
 * @author Tommeh | 25-11-2018 | 19:18
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class DustDevilProcessor extends DropProcessor {

    @Override
    public void attach() {
        //Dust battlestaff
        appendDrop(new DisplayedDrop(20736, 1, 1, 3000));
        //Dragon chainbody
        appendDrop(new DisplayedDrop(3140, 1, 1, 32768));
    }

    @Override
    public void onDeath(final NPC npc, final Player killer) {
        SherlockTask.SLAY_DUST_DEVIL.progress(killer);
    }

    @Override
    public Item drop(final NPC npc, final Player killer, final Drop drop, final Item item) {
        if (!drop.isAlways()) {
            if (random(32768) == 0) {
                return new Item(3140);
            }
            if (random(3000) == 0) {
                return new Item(20736);
            }
        }
        return item;
    }

    @Override
    public int[] ids() {
        return new int[] { 423, 7249 };
    }
}
