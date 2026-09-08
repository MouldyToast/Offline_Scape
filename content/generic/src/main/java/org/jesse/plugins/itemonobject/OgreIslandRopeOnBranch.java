package org.jesse.plugins.itemonobject;

import org.jesse.game.content.skills.agility.shortcut.OgreIslandEntranceRopeSwing;
import org.jesse.game.item.Item;
import org.jesse.game.item.ids.ItemId;
import org.jesse.game.model.item.ItemOnObjectAction;
import org.jesse.game.world.entity.pathfinding.events.player.TileEvent;
import org.jesse.game.world.entity.pathfinding.strategy.TileStrategy;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Christopher
 * @since 1/26/2020
 */
public class OgreIslandRopeOnBranch implements ItemOnObjectAction {
    private static final OgreIslandEntranceRopeSwing SHORTCUT = new OgreIslandEntranceRopeSwing();
    @Override
    public void handleItemOnObjectAction(Player player, Item item, int slot, WorldObject object) {
        SHORTCUT.handle(player, object,  0, null);
    }

    @Override
    public Object[] getItems() {
        return new Object[] { ItemId.ROPE };
    }

    @Override
    public Object[] getObjects() {
        return new Object[] { OgreIslandEntranceRopeSwing.BRANCH };
    }

    public void handle(final Player player, final Item item, int slot, final WorldObject object) {
        player.setRouteEvent(new TileEvent(player, new TileStrategy(OgreIslandEntranceRopeSwing.startPosition), () -> {
            player.stopAll();
            player.setFaceLocation(OgreIslandEntranceRopeSwing.destination);
            handleItemOnObjectAction(player, item, slot, object);
        }));
    }
}
