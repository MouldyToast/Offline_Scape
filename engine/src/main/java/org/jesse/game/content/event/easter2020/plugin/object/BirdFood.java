package org.jesse.game.content.event.easter2020.plugin.object;

import org.jesse.game.item.Item;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.world.object.WorldObject;
import org.jesse.plugins.SkipPluginScan;
import org.jesse.plugins.dialogue.ItemChat;
import org.jesse.plugins.dialogue.PlayerChat;

/**
 * @author Corey
 * @since 02/04/2020
 */
@SkipPluginScan
public class BirdFood implements ObjectAction {
    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        final SeedBowl.Contents bag = SeedBowl.Contents.byFoodBagId.get(object.getId());
        final int item = bag.getFoodItemId();
        if (player.getInventory().containsItem(item)) {
            player.getDialogueManager().start(new PlayerChat(player, "I already have some of that."));
            return;
        }
        if (player.getInventory().addItem(new Item(item)).isFailure()) {
            player.sendMessage("You do not have room for any bird food.");
        } else {
            // TODO add animation for taking something from the bag
            player.getDialogueManager().start(new ItemChat(player, new Item(item), "You pick up some of the bird food."));
        }
    }

    @Override
    public Object[] getObjects() {
        return SeedBowl.Contents.byFoodBagId.keySet().stream().filter(i -> i != -1).toArray();
    }
}
