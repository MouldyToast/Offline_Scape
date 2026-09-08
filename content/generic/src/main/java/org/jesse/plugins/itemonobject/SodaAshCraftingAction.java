package org.jesse.plugins.itemonobject;

import org.jesse.game.content.skills.crafting.actions.SodaAshCrafting;
import org.jesse.game.item.Item;
import org.jesse.game.model.item.ItemOnObjectAction;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.WorldObject;
import org.jesse.plugins.dialogue.ItemChat;
import org.jesse.plugins.dialogue.skills.SodaAshD;

/**
 * @author Noele
 * see https://noeles.life || noele@zenyte.com
 */
public class SodaAshCraftingAction implements ItemOnObjectAction {

    @Override
    public void handleItemOnObjectAction(Player player, Item item, int slot, WorldObject object) {
        if (!player.getInventory().containsItem(SodaAshCrafting.SEAWEED)) {
            player.getDialogueManager().start(new ItemChat(player, SodaAshCrafting.SEAWEED, "You need seaweed to make soda ash."));
            return;
        }
        player.getDialogueManager().start(new SodaAshD(player, object.getName().toLowerCase().equals("range")));
    }

    @Override
    public Object[] getItems() {
        return new Object[] { 401 };
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { "Range", "Fire" };
    }
}
