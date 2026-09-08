package org.jesse.game.content.event.christmas2019;

import org.jesse.game.item.Item;
import org.jesse.game.item.ids.ItemId;
import org.jesse.game.util.Utils;
import org.jesse.game.world.WorldThread;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Kris | 17/12/2019
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class SnowObject implements ObjectAction {

    private static final Animation animation = new Animation(15095);

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (!player.getInventory().hasFreeSlots() && !player.getInventory().containsItem(ItemId.SNOWBALL, 1)) {
            player.sendMessage("You need some free space to collect the snow.");
            return;
        }
        if (player.getNumericTemporaryAttribute("snowball collection delay").intValue() >= WorldThread.getCurrentCycle()) {
            return;
        }
        player.addTemporaryAttribute("snowball collection delay", WorldThread.getCurrentCycle() + 2);
        player.faceObject(object);
        player.setInvalidAnimation(animation);
        player.getInventory().addItem(new Item(ItemId.SNOWBALL, Utils.random(3, 10)));
    }

    @Override
    public int getDelay() {
        return 1;
    }

    @Override
    public Object[] getObjects() {
        return new Object[] {
                ChristmasConstants.SNOW_OBJECT
        };
    }
}
