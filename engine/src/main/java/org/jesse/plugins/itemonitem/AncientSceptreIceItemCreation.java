package org.jesse.plugins.itemonitem;

import org.jesse.game.item.Item;
import org.jesse.game.item.ids.ItemId;
import org.jesse.game.model.item.ItemOnItemAction;
import org.jesse.game.world.entity.player.Player;
import org.jesse.plugins.item.sceptre.AncientSceptre;

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-08-29
 */
public class AncientSceptreIceItemCreation extends AncientSceptre implements ItemOnItemAction {
    @Override
    public int[] getItems() {
        return new int[] { ItemId.ICE_QUARTZ, ItemId.ANCIENT_SCEPTRE };
    }

    @Override
    public void handleItemOnItemAction(Player player, Item from, Item to, int fromSlot, int toSlot) {
        var gem = from.getId() == ItemId.ICE_QUARTZ ? from : to;
        var sceptre = to.getId() == ItemId.ANCIENT_SCEPTRE ? to : from;

        player.getDialogueManager().start(getScepterCombiningDialogue(player, gem, sceptre, ItemId.ICE_ANCIENT_SCEPTRE_28262));
    }
}
