package org.jesse.game.world.object.ladders;

import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.Ladder;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.world.object.WorldObject;

public class PirateShipLadders implements ObjectAction {
    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        int id = object.getId();
        if (id == 272) {
            Ladder.use(player, "Climb-up", object, player.getLocation().transform(0, 0, 1));
        }
        if(id == 273) {
            Ladder.use(player, "Climb-down", object,  player.getLocation().transform(0, 0, -1));
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[]{
                272,
                273
        };
    }
}
