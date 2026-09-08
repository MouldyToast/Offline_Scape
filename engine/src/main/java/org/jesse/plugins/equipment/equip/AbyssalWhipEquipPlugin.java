package org.jesse.plugins.equipment.equip;

import org.jesse.game.content.treasuretrails.clues.SherlockTask;
import org.jesse.game.item.Item;
import org.jesse.game.world.entity.player.Player;

/**
 * @author Kris | 10/04/2019 20:26
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class AbyssalWhipEquipPlugin implements EquipPlugin {
    @Override
    public boolean handle(final Player player, final Item item, final int slotId, final int equipmentSlot) {
        if (slotId != -1) {
            SherlockTask.EQUIP_ABYSSAL_WHIP_INFRONT_OF_DEMONS.progress(player);
        }
        return true;
    }

    @Override
    public int[] getItems() {
        return new int[] {
                4151, 4178, 12773, 12774, 20405
        };
    }
}
