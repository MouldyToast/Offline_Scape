package org.jesse.game.content.skills.construction.objects.questhall;

import org.jesse.game.content.skills.construction.Construction;
import org.jesse.game.content.skills.construction.ObjectInteraction;
import org.jesse.game.content.skills.construction.RoomReference;
import org.jesse.game.task.WorldTask;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.masks.Graphics;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Kris | 24. veebr 2018 : 23:03.22
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 * TODO: Restrictions.
 */
public final class MythicalCape implements ObjectInteraction {

    private static final Animation START = new Animation(714);

    private static final Graphics GFX = new Graphics(308, 48, 100);

    private static final Location TILE = new Location(2457, 2856, 0);

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.MYTHICAL_CAPE };
    }

    @Override
    public void handleObjectAction(Player player, Construction construction, RoomReference reference, WorldObject object, int optionId, String option) {
        player.lock();
        player.setAnimation(START);
        player.setGraphics(GFX);
        WorldTasksManager.schedule(new WorldTask() {

            @Override
            public void run() {
                player.unlock();
                player.setLocation(TILE);
                player.setAnimation(Animation.STOP);
            }
        }, 2);
    }
}
