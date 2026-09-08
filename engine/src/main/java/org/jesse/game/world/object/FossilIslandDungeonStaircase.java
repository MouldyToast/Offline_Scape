package org.jesse.game.world.object;

import org.jesse.game.obj.ids.ObjectId;

import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.player.Player;

/**
 * @author Kris | 29/02/2020
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class FossilIslandDungeonStaircase implements ObjectAction {

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        player.setLocation(new Location(player.getX() > object.getX() ? 3603 : 3607, 10291, 0));
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.STAIRS_31485 };
    }
}
