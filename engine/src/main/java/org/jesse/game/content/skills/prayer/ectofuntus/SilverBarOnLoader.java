package org.jesse.game.content.skills.prayer.ectofuntus;

import org.jesse.game.item.Item;
import org.jesse.game.model.item.ItemOnObjectAction;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.player.Action;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Kris | 26/06/2019 13:00
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class SilverBarOnLoader implements ItemOnObjectAction {

    private static final Animation deposit = new Animation(1649);

    @Override
    public void handleItemOnObjectAction(final Player player, final Item item, final int slot, final WorldObject object) {
        player.getActionManager().setAction(new Action() {

            @Override
            public boolean start() {
                return true;
            }

            @Override
            public boolean process() {
                return player.getInventory().containsItem(item);
            }

            @Override
            public int processWithDelay() {
                player.setFaceLocation(new Location(3660, 3525, 1));
                player.setAnimation(deposit);
                player.getInventory().deleteItem(item);
                WorldTasksManager.schedule(() -> player.getInventory().addOrDrop(new Item(7650, 1)), 1);
                return 4;
            }
        });
    }

    @Override
    public Object[] getItems() {
        return new Object[] { 2355 };
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.LOADER };
    }
}
