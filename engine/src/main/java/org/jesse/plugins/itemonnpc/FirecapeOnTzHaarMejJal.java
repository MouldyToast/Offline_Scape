package org.jesse.plugins.itemonnpc;

import org.jesse.game.item.Item;
import org.jesse.game.model.item.ItemOnNPCAction;
import org.jesse.game.util.Colour;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.plugins.dialogue.TzHaarMejJalD;

/**
 * @author Noele
 * see https://noeles.life || noele@zenyte.com
 */
public class FirecapeOnTzHaarMejJal implements ItemOnNPCAction {

    @Override
    public void handleItemOnNPCAction(final Player player, final Item item, final int slot, final NPC npc) {
        if (!player.getInventory().hasFreeSlots()) {
            player.sendMessage(Colour.RS_RED.wrap("You need to make space in your inventory!"));
            return;
        }
        player.getDialogueManager().start(new TzHaarMejJalD(player, npc, true));
    }

    @Override
    public Object[] getItems() {
        return new Object[] { 6570 };
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.CATAPULT_2180 };
    }
}
