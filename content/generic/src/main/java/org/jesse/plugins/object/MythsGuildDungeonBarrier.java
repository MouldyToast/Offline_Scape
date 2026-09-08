package org.jesse.plugins.object;

import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Kris | 10/05/2019 19:56
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class MythsGuildDungeonBarrier implements ObjectAction {

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (option.equalsIgnoreCase("Pass")) {
            player.lock(2);
            player.setRunSilent(2);
            if (player.getX() < object.getX()) {
                player.addWalkSteps(player.getX() + 2, player.getY(), -1, false);
            } else {
                player.addWalkSteps(player.getX() - 2, player.getY(), -1, false);
            }
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.MAGICAL_BARRIER_31617 };
    }
}
