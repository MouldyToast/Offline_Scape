package org.jesse.plugins.object;

import org.jesse.game.item.ids.ItemId;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Chris
 * @since August 18 2020
 */
public class MageBankKnifeSack implements ObjectAction {
    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if (!player.carryingAny(ItemId.KNIFE)) {
            player.sendMessage("You find a knife inside the sack.");
            player.getInventory().addOrDrop(ItemId.KNIFE);
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[]{ObjectId.SACK_14743};
    }
}
