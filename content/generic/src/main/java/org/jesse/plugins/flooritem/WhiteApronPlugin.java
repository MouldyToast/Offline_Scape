package org.jesse.plugins.flooritem;

import org.jesse.game.item.Item;
import org.jesse.game.item.ids.ItemId;
import org.jesse.game.world.World;
import org.jesse.game.world.entity.SoundEffect;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.flooritem.FloorItem;
import org.jetbrains.annotations.NotNull;

/**
 * @author Corey
 * @since 15/06/2020
 */
public class WhiteApronPlugin implements FloorItemPlugin {
    
    @Override
    public void handle(Player player, FloorItem item, int optionId, String option) {
        if (player.getInventory().addItem(new Item(ItemId.WHITE_APRON)).isFailure()) {
            player.sendFilteredMessage("Not enough space in your inventory to pick the item up.");
        } else {
            World.destroyFloorItem(item);
            player.sendSound(new SoundEffect(2582));
        }
    }
    
    @Override
    public int[] getItems() {
        return new int[]{ItemId.WHITE_APRON_7957};
    }
    
    @Override
    public boolean overrideTake() {
        return true;
    }
    
    @Override
    public void telegrab(@NotNull Player player, @NotNull FloorItem item) {
        World.destroyFloorItem(item);
        player.getInventory().addItem(new Item(ItemId.WHITE_APRON)).onFailure(it -> World.spawnFloorItem(it, player));
    }
}
