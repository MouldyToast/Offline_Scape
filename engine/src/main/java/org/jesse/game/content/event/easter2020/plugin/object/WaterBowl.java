package org.jesse.game.content.event.easter2020.plugin.object;

import org.jesse.game.content.event.easter2020.EasterConstants;
import org.jesse.game.content.event.easter2020.plugin.npc.EasterBird;
import org.jesse.game.item.Item;
import org.jesse.game.item.ids.ItemId;
import org.jesse.game.model.item.ItemOnObjectAction;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.WorldObject;
import org.jesse.plugins.SkipPluginScan;
import org.jesse.plugins.dialogue.PlainChat;

/**
 * @author Corey
 * @since 02/04/2020
 */
@SkipPluginScan
public class WaterBowl implements ItemOnObjectAction {
    
    public static void waterOnBowl(final Player player, final Item item) {
        if (EasterBird.Varbit.WATER_BOWL.isSet(player)) {
            player.getDialogueManager().start(new PlainChat(player, "The bowl is already full of water."));
            return;
        }
        
        EasterBird.Varbit.WATER_BOWL.sendVar(player);
        player.getDialogueManager().start(new PlainChat(player, "You fill the bowl with water."));
        player.getInventory().deleteItem(item);
    }
    
    @Override
    public void handleItemOnObjectAction(Player player, Item item, int slot, WorldObject object) {
        waterOnBowl(player, item);
    }
    
    @Override
    public Object[] getItems() {
        return new Object[]{ItemId.BUCKET_OF_WATER};
    }
    
    @Override
    public Object[] getObjects() {
        return new Object[]{EasterConstants.WATER_BOWL};
    }
}
