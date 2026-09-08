package org.jesse.plugins.itemonobject;

import org.jesse.game.item.Item;
import org.jesse.game.model.item.ItemOnObjectAction;
import org.jesse.game.task.WorldTasksManager;
import org.jesse.game.world.entity.Location;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.pathfinding.events.player.ObjectEvent;
import org.jesse.game.world.entity.pathfinding.strategy.ObjectStrategy;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.WorldObject;
import org.jesse.game.world.object.WorldObjectUtils;

/**
 * @author Noele
 * see https://noeles.life || noele@zenyte.com
 */
public class WaterBucketOnBarDispenser implements ItemOnObjectAction {

    private static final Animation THROW_WATER = new Animation(2450);

    private static final Location FACE_TILE = new Location(1940, 4963, 0);

    @Override
    public void handleItemOnObjectAction(final Player player, final Item item, final int slot, final WorldObject object) {
        if (WorldObjectUtils.getObjectIdOfPlayer(object, player) == 9095) {
            player.setRouteEvent(new ObjectEvent(player, new ObjectStrategy(object), () -> {
                player.setFaceLocation(FACE_TILE);
                player.setAnimation(THROW_WATER);
                WorldTasksManager.schedule(() -> {
                    player.getInventory().replaceItem(1925, 1, player.getInventory().getContainer().getSlot(item));
                    player.getBlastFurnace().setDispenser(3);
                    player.getBlastFurnace().setEarlyCool(true);
                }, 0);
            }));
        }
    }

    @Override
    public Object[] getItems() {
        return new Object[] { 1929 };
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { 9092 };
    }
}
