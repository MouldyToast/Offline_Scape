package org.jesse.plugins.object;

import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Kris | 27/04/2019 02:14
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class LighthouseIronLadder implements ObjectAction {

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (option.equals("Climb")) {
            final boolean up = player.getY() <= 4629;
            player.setAnimation(new Animation(up ? 827 : 828));
            player.lock(2);
            WorldTasksManager.schedule(() -> player.setLocation(new Location(2515, up ? 10008 : 4629, 0)));
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { 4485, ObjectId.IRON_LADDER_4413 };
    }
}
