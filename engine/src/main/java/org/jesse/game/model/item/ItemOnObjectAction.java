package org.jesse.game.model.item;

import org.jesse.game.item.Item;
import org.jesse.game.world.entity.pathfinding.events.player.ObjectEvent;
import org.jesse.game.world.entity.pathfinding.strategy.ObjectStrategy;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.WorldObject;
import org.jesse.plugins.Plugin;

/**
 * @author Kris | 10. nov 2017 : 23:12.29
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 */
public interface ItemOnObjectAction extends Plugin {

	void handleItemOnObjectAction(final Player player, final Item item, int slot, final WorldObject object);

	Object[] getItems();

	Object[] getObjects();

	default ObjectStrategy getObjectStrategy(final WorldObject obj) {
		final int strategyDistance = getObjectStrategyDistance(obj);
		return new ObjectStrategy(obj, strategyDistance);
	}

	default int getObjectStrategyDistance(final WorldObject obj) {
		return 0;
	}

	default void handle(final Player player, final Item item, int slot, final WorldObject object) {
		player.setRouteEvent(new ObjectEvent(player, getObjectStrategy(object), () -> {
			player.stopAll();
			player.faceObject(object);
			if (player.getInventory().getItem(slot) != item) {
				return;
			}
			handleItemOnObjectAction(player, item, slot, object);
		}));
	}

}
