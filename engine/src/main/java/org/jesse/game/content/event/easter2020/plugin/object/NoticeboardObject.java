package org.jesse.game.content.event.easter2020.plugin.object;

import org.jesse.game.GameInterface;
import org.jesse.game.content.event.easter2020.EasterConstants;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.pathfinding.events.player.TileEvent;
import org.jesse.game.world.entity.pathfinding.strategy.TileStrategy;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.world.object.WorldObject;
import org.jesse.plugins.SkipPluginScan;

/**
 * @author Corey
 * @since 29/03/2020
 */
@SkipPluginScan
public class NoticeboardObject implements ObjectAction {
    @Override
    public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
        GameInterface.EASTER_NOTICEBOARD.open(player);
    }

    @Override
    public Object[] getObjects() {
        return new Object[] {EasterConstants.NOTICEBOARD};
    }

    @Override
    public void handle(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
        final int xCoord = player.getPosition().getX() < 2203 ? 2202 : 2203;
        player.setRouteEvent(new TileEvent(player, new TileStrategy(new Location(xCoord, 4400)), getRunnable(player, object, name, optionId, option), getDelay()));
    }
}
