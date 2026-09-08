package org.jesse.game.content.treasuretrails;

import org.jesse.game.GameInterface;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Kris | 04/01/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class TreasureTrailStatisticsObject implements ObjectAction {

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        GameInterface.TREASURE_TRAIL_STATISTICS.open(player);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.TREASURE_TRAIL_STATISTICS_26634, ObjectId.TREASURE_TRAIL_STATISTICS };
    }
}
