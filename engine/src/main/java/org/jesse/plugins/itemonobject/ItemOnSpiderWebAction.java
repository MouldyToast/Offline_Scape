package org.jesse.plugins.itemonobject;

import org.jesse.game.item.Item;
import org.jesse.game.item.ids.ItemId;
import org.jesse.game.model.item.ItemOnObjectAction;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;
import org.jesse.plugins.object.SpiderWebObjectAction;

/**
 * @author Kris | 06/05/2019 21:39
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ItemOnSpiderWebAction implements ItemOnObjectAction {

    @Override
    public void handleItemOnObjectAction(final Player player, final Item item, final int slot, final WorldObject object) {
        final String name = item.getName().toLowerCase();
        if (item.getDefinitions().getSlot() != -1 && SpiderWebObjectAction.sharpBladePredicate.test(item)) {
            SpiderWebObjectAction.slash(player, object, item);
            return;
        }
        if(item.getId() == ItemId.KNIFE) {
            SpiderWebObjectAction.slash(player, object, new Item(ItemId.KNIFE));
            return;
        }
        player.sendMessage("Only a sharp blade can cut through this sticky web.");
    }

    @Override
    public Object[] getItems() {
        return null;
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.WEB };
    }
}
