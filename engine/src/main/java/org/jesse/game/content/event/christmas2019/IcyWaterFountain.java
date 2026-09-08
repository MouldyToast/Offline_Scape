package org.jesse.game.content.event.christmas2019;

import org.jesse.game.item.Item;
import org.jesse.game.item.ids.ItemId;
import org.jesse.game.model.item.ItemOnObjectAction;
import org.jesse.game.world.entity.SoundEffect;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Corey
 * @since 20/12/2019
 */
public class IcyWaterFountain implements ItemOnObjectAction {
    
    @Override
    public void handleItemOnObjectAction(Player player, Item item, int slot, WorldObject object) {
        player.getInventory().set(slot, new Item(ChristmasConstants.ICY_WATER_BUCKET));
        player.getPacketDispatcher().sendSoundEffect(new SoundEffect(2609));
        player.setAnimation(new Animation(832));
        player.sendFilteredMessage("You fill the bucket with icy water from the fountain.");
    }
    
    @Override
    public Object[] getItems() {
        return new Object[]{ItemId.BUCKET};
    }
    
    @Override
    public Object[] getObjects() {
        return new Object[]{ChristmasConstants.ICY_WATER_FOUNTAIN};
    }
    
}
