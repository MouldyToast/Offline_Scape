package org.jesse.game.content.godwars.objects;

import org.jesse.game.model.ui.InterfacePosition;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Tommeh | 24-3-2019 | 13:38
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class GodwarsExitRopeObject implements ObjectAction {

    private static final Location OUTSIDE_LOCATION = new Location(2916, 3745, 0);

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        player.getInterfaceHandler().closeInterface(InterfacePosition.OVERLAY);
        player.useStairs(828, OUTSIDE_LOCATION, 1, 0, "You climb up the rope.");
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.ROPE_26370 };
    }
}
