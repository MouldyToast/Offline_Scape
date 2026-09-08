package org.jesse.plugins.object;

import org.jesse.game.item.Item;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.FillContainer;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Noele
 * see https://noeles.life || noele@zenyte.com
 */
public class BlastFurnaceSink implements ObjectAction {

    private static final Item BUCKET = new Item(1925, 1);

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (player.getInventory().containsItem(1925, 1)) {
            player.getActionManager().setAction(new FillContainer(object, BUCKET));
            return;
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.SINK_9143 };
    }
}
