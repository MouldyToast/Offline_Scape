package org.jesse.plugins.object;

import org.jesse.game.content.skills.mining.actions.DenseRunestoneMining;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;

public class DenseRunestoneObjectAction  implements ObjectAction {
    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        player.getActionManager().setAction(new DenseRunestoneMining(object));
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.DENSE_RUNESTONE };
    }
}
