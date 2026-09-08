package org.jesse.game.content.kebos.konar.plugins.objects;

import org.jesse.game.task.WorldTask;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.util.DirectionUtil;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Tommeh | 24/10/2019 | 22:54
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>
 */
public class TunnelObstacle implements ObjectAction {

    private static final Animation crawlAnim = new Animation(2796);

    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        final Location currentTile = new Location(player.getLocation());
        final Location destination = currentTile.transform(player.getX() < object.getX() ? 7 : -7, 0, 0);
        final int direction = DirectionUtil.getFaceDirection(destination.getX() - currentTile.getX(), destination.getY() - currentTile.getY());
        player.lock();
        WorldTasksManager.schedule(new WorldTask() {

            int ticks;

            @Override
            public void run() {
                switch(ticks++) {
                    case 1:
                        player.setAnimation(crawlAnim);
                        player.autoForceMovement(destination, 0, 90, direction);
                        break;
                    case 4:
                        player.unlock();
                        stop();
                        break;
                }
            }
        }, 0, 0);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.TUNNEL_34516 };
    }
}
