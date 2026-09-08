package org.jesse.plugins.object;

import org.jesse.game.content.skills.magic.spells.teleports.TeleportCollection;
import org.jesse.game.world.entity.pathfinding.events.player.TileEvent;
import org.jesse.game.world.entity.pathfinding.strategy.TileStrategy;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.obj.ids.ObjectId;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Kris | 28/04/2019 19:15
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
public class CentreOfCropCircle implements ObjectAction {

    @Override
    public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        if (option.equals("Enter")) {
            TeleportCollection.PURO_CENTER_OF_CROP_CIRCLE.teleport(player);
        }
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { ObjectId.CENTRE_OF_CROP_CIRCLE, ObjectId.CENTRE_OF_CROP_CIRCLE_24991 };
    }

    @Override
    public void handle(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        player.setRouteEvent(new TileEvent(player, new TileStrategy(object), getRunnable(player, object, name, optionId, option), getDelay()));
    }
}
