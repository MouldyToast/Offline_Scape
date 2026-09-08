package org.jesse.game.content.event.easter2020.plugin.object;

import org.jesse.game.content.event.easter2020.EasterConstants;
import org.jesse.game.item.Item;
import org.jesse.game.item.ids.ItemId;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.world.object.WorldObject;
import org.jesse.plugins.SkipPluginScan;

/**
 * @author Corey
 * @since 02/04/2020
 */
@SkipPluginScan
public class WaterTank implements ObjectAction {
    
    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (player.getInventory().addItem(new Item(ItemId.BUCKET_OF_WATER)).isFailure()) {
            player.sendMessage("You do not have enough space in your inventory to do this.");
        } else {
            player.setAnimation(new Animation(827));
            player.sendMessage("You fill a bucket of water from the tank.");
        }
    }
    
    @Override
    public Object[] getObjects() {
        return new Object[]{EasterConstants.WATER_TANK};
    }
    
}
