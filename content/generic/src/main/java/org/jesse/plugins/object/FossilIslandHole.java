package org.jesse.plugins.object;

import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Kris | 10/05/2019 20:41
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class FossilIslandHole implements ObjectAction {

    private static final Animation CRAWL = new Animation(844);

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        player.lock(2);
        player.setAnimation(CRAWL);
        WorldTasksManager.schedule(() -> {
            if (object.getId() == ObjectId.HOLE_31482) {
                player.setLocation(new Location(3715, 3815, 0));
            } else {
                player.setLocation(new Location(3713, 3830, 0));
            }
        }, 1);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.HOLE_31481, ObjectId.HOLE_31482 };
    }
}
