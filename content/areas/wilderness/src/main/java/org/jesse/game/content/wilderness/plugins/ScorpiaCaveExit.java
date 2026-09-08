package org.jesse.game.content.wilderness.plugins;

import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Kris | 16/03/2019 20:27
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class ScorpiaCaveExit implements ObjectAction {

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        if ("Use".equals(option)) {
            if (object.getId() == ObjectId.CREVICE_26763) {
                if (object.getX() == 3233 && object.getY() == 10331) {
                    player.useStairs(2796, new Location(3233, 3938, 0), 1, 2, null, true);
                } else if (object.getX() == 3232 && object.getY() == 10352) {
                    player.useStairs(2796, new Location(3233, 3950, 0), 1, 2, null, true);
                } else if (object.getX() == 3243 && object.getY() == 10352) {
                    player.useStairs(2796, new Location(3242, 3948, 0), 1, 2, null, true);
                }
            }
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.CREVICE_26763 };
    }
}
