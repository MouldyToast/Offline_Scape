package org.jesse.game.content.skills.hunter.aerialfishing.object;

import org.jesse.game.item.ids.ItemId;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.container.impl.Inventory;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Cresinkel
 */

public class KingWormObjectAction implements ObjectAction {
    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (option.equals("Take")) {
            final Inventory inven = player.getInventory();
            if (inven.hasFreeSlots()) {
                player.setAnimation(Animation.GRAB);
                inven.addItem(ItemId.KING_WORM, 1);
            } else {
                player.sendMessage("You can not carry anymore King Worm's.");
            }
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] {ObjectId.KING_WORM};
    }
}
