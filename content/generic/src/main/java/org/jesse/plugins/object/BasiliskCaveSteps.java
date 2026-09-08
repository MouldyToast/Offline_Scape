package org.jesse.plugins.object;

import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.world.object.WorldObject;

public class BasiliskCaveSteps implements ObjectAction {
    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if(player.getX() == 2471) {
            player.lock(1);
            player.setLocation(new Location(2468, 10403, 0));
        } else if(player.getX() == 2468) {
            player.lock(1);
            player.setLocation(new Location(2471, 10403, 0));
        } else {
            player.sendDeveloperMessage("Unknown current location : " + player.getLocation());
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] {37417};
    }
}
