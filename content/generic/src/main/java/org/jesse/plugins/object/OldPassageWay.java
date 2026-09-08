package org.jesse.plugins.object;

import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Kris | 19/04/2019 17:55
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class OldPassageWay implements ObjectAction {

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (option.equalsIgnoreCase("Leave")) {
            if (object.matches(new Location(3013, 9951, 0))) {
                player.setLocation(new Location(3037, 3382, 0));
            } else {
                player.setLocation(new Location(3054, 3382, 0));
            }
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.OLD_PASSAGEWAY_31892 };
    }
}
