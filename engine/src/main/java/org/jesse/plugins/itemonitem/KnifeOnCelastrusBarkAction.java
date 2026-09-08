package org.jesse.plugins.itemonitem;

import org.jesse.game.item.Item;
import org.jesse.game.item.ids.ItemId;
import org.jesse.game.model.item.ItemOnItemAction;
import org.jesse.game.world.entity.player.Player;
import org.jesse.plugins.dialogue.skills.CelastrusBarkFletchingD;

/**
 * @author Tommeh | 19/11/2019 | 21:28
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class KnifeOnCelastrusBarkAction implements ItemOnItemAction {

    @Override
    public void handleItemOnItemAction(Player player, Item from, Item to, int fromSlot, int toSlot) {
        player.getDialogueManager().start(new CelastrusBarkFletchingD(player));
    }

    @Override
    public int[] getItems() {
        return new int[] {ItemId.CELASTRUS_BARK, ItemId.KNIFE };
    }
}
