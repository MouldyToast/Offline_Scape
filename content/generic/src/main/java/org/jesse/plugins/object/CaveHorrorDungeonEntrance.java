package org.jesse.plugins.object;

import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Tommeh | 18-12-2018 | 16:45
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class CaveHorrorDungeonEntrance implements ObjectAction {

    private static final Location INSIDE_TILE = new Location(3748, 9374, 0);

    private static final Location OUTSIDE_TILE = new Location(3749, 2973, 0);

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        player.setLocation(object.getId() == 3650 ? INSIDE_TILE : OUTSIDE_TILE);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.CAVE_ENTRANCE_3650, ObjectId.CAVE_5553, ObjectId.CAVE_6702 };
    }
}
