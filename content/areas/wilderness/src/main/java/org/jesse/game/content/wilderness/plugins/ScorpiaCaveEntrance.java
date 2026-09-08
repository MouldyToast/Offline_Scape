package org.jesse.game.content.wilderness.plugins;

import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Kris | 16/03/2019 20:23
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ScorpiaCaveEntrance implements ObjectAction {

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if ("Enter".equals(option)) {
            if (object.getX() == 3231 && object.getY() == 3936) {
                player.useStairs(2796, new Location(3233, 10332, 0), 1, 2, null, true);
            } else if (object.getX() == 3231 && object.getY() == 3951) {
                player.useStairs(2796, new Location(3232, 10351, 0), 1, 2, null, true);
            } else if (object.getX() == 3241 && object.getY() == 3949) {
                player.useStairs(2796, new Location(3243, 10351, 0), 1, 2, null, true);
            }
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.CAVERN };
    }
}
