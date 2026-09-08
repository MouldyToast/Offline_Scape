package org.jesse.plugins.drop.thermonuclearsmokedevil;

import org.jesse.game.item.Item;
import org.jesse.game.item.ids.ItemId;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.npc.drop.matrix.Drop;
import org.jesse.game.world.entity.npc.drop.matrix.DropProcessor;
import org.jesse.game.world.entity.player.Player;

/**
 * @author Tommeh | 25-11-2018 | 19:22
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
@SuppressWarnings("unused")
public class ThermonuclearSmokeDevilProcessor extends DropProcessor {

    @Override
    public void attach() {
        //Occult necklace
        appendDrop(new DisplayedDrop(12002, 1, 1, 150));
        //Smoke battlestaff
        appendDrop(new DisplayedDrop(11998, 1, 1, 250));
        //Dragon chainbody
        appendDrop(new DisplayedDrop(3140, 1, 1, 200));

        appendDrop(new DisplayedDrop(ItemId.JAR_OF_SMOKE, 1, 1, 600));
    }

    @Override
    public void onDeath(final NPC npc, final Player killer) {
        if(randomDrop(killer, 600) == 0) {
            npc.dropItem(killer, new Item(ItemId.JAR_OF_SMOKE));
        }
    }

    @Override
    public Item drop(final NPC npc, final Player killer, final Drop drop, final Item item) {
        if (!drop.isAlways()) {
            if (randomDrop(killer, 200) == 0) {
                return new Item(3140);
            }
            if (randomDrop(killer,250) == 0) {
                return new Item(11998);
            }
            if (randomDrop(killer,150) == 0) {
                return new Item(12002);
            }
        }
        return item;
    }

    @Override
    public int[] ids() {
        return new int[] { 499 };
    }
}
