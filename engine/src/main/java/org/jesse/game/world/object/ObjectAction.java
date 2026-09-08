package org.jesse.game.world.object;

import org.jesse.game.world.World;
import org.jesse.game.world.entity.pathfinding.events.player.ObjectEvent;
import org.jesse.game.world.entity.pathfinding.strategy.ObjectStrategy;
import org.jesse.game.world.entity.player.Player;
import org.jesse.plugins.Plugin;

/**
 * @author Kris | 10. nov 2017 : 21:38.45
 * @author Jire
 */
public interface ObjectAction extends Plugin {

    void handleObjectAction(final Player player, final WorldObject object, final String name,
                            final int optionId, final String option);

    Object[] getObjects();

    default int getDelay() {
        return 0;
    }

    default void handle(final Player player, final WorldObject object, final String name,
                        final int optionId, final String option) {
        final ObjectStrategy strategy = getStrategy(object);
        final Runnable runnable = getRunnable(player, object, name, optionId, option);
        final int delay = getDelay();

        final ObjectEvent objectEvent = new ObjectEvent(player, strategy, runnable, delay);
        player.setRouteEvent(objectEvent);
    }

    default ObjectStrategy getStrategy(final WorldObject obj) {
        final int strategyDistance = getStrategyDistance(obj);
        return new ObjectStrategy(obj, strategyDistance);
    }

    default int getStrategyDistance(final WorldObject obj) {
        return 0;
    }

    default Runnable getRunnable(final Player player, final WorldObject object, final String name,
                                 final int optionId, final String option) {
        return () -> {
            if (player.getPlane() != object.getPlane()) return;
            final WorldObject existingObject = World.getObjectWithId(object, object.getId());
            if (existingObject == null) return;
            player.stopAll();
            player.faceObject(object);
            if (ObjectHandler.handleOptionClick(player, optionId, object)) {
                handleObjectAction(player, object, name, optionId, option);
            }
        };
    }

    default void init() {
    }

}
