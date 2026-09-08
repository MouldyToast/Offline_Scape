package org.jesse.game.content.minigame.pestcontrol.plugins;

import org.jesse.game.content.minigame.pestcontrol.PestControlInstance;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;
import org.jesse.game.world.region.RegionArea;

/**
 * @author Kris | 27. juuni 2018 : 14:42:15
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public final class WatchtowerLadderPlugin implements ObjectAction {

    private static final Animation CLIMB_UP = new Animation(828);

    private static final Animation CLIMB_DOWN = new Animation(827);

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        final RegionArea area = player.getArea();
        if (!(area instanceof PestControlInstance)) {
            return;
        }
        final PestControlInstance instance = (PestControlInstance) area;
        final boolean atWatchtower = instance.isAtWatchtower(player);
        player.setAnimation(atWatchtower ? CLIMB_DOWN : CLIMB_UP);
        player.lock();
        WorldTasksManager.schedule(() -> {
            player.unlock();
            switch(object.getRotation()) {
                case 0:
                    player.setLocation(new Location(object.getX(), object.getY() + (atWatchtower ? 1 : -1), object.getPlane()));
                    return;
                case 1:
                    player.setLocation(new Location(object.getX() + (atWatchtower ? 1 : -1), object.getY(), object.getPlane()));
                    return;
                case 3:
                    player.setLocation(new Location(object.getX() + (atWatchtower ? -1 : 1), object.getY(), object.getPlane()));
                    return;
            }
        });
    }

    @Override
    public int getDelay() {
        return 1;
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.LADDER_14296 };
    }
}
