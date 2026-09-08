package org.jesse.plugins.object;

import org.jesse.game.item.Item;
import org.jesse.game.item.ids.ItemId;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;
import org.jesse.plugins.dialogue.ItemChat;
import org.jesse.plugins.dialogue.PlayerChat;


public final class CorpseCartPlugin implements ObjectAction {

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (option.equals("Search")) {
            Item item = new Item(ItemId.SEVERED_LEG_24792);
            long lastSearch = (long) player.getTemporaryAttributes().getOrDefault("CORPSE_CART", 0L);
            boolean has = player.getInventory().containsItem(item) || player.getBank().containsItem(item) || player.getEquipment().containsItem(item);
            if(!has) {
                if (player.getInventory().hasSpaceFor(item)) {
                    player.getTemporaryAttributes().put("CORPSE_CART", System.currentTimeMillis());
                    player.getDialogueManager().start(new ItemChat(player, item, "For some reason, you take a severed leg."));
                    player.getInventory().addItem(item);
                } else {
                    player.sendMessage("You don't have the space to do this.");
                }
            } else {
                player.getDialogueManager().start(new PlayerChat(player, "I probably shouldn't take any more of these."));
            }
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.CORPSE_CART_39161 , ObjectId.CORPSE_CART};
    }
}
