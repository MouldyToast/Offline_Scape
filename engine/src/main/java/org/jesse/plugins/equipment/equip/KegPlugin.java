package org.jesse.plugins.equipment.equip;

import org.jesse.game.content.minigame.warriorsguild.kegbalance.KegBalanceArea;
import org.jesse.game.item.Item;
import org.jesse.game.item.ids.ItemId;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.region.GlobalAreaManager;

/**
 * @author Kris | 29/07/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class KegPlugin implements EquipPlugin {
    @Override
    public boolean handle(Player player, Item item, int slotId, int equipmentSlot) {
        GlobalAreaManager.getArea(KegBalanceArea.class).loseBalance(player);
        return false;
    }

    @Override
    public int[] getItems() {
        return new int[] {
                ItemId.ONE_BARREL, ItemId.TWO_BARRELS, ItemId.THREE_BARRELS, ItemId.FOUR_BARRELS, ItemId.FIVE_BARRELS
        };
    }
}
