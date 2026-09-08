package org.jesse.game.model.item;

import org.jesse.game.item.Item;
import org.jesse.game.world.entity.npc.NPC;
import org.jesse.game.world.entity.pathfinding.events.player.EntityEvent;
import org.jesse.game.world.entity.pathfinding.strategy.EntityStrategy;
import org.jesse.game.world.entity.player.Player;
import org.jesse.plugins.Plugin;

/**
 * @author Kris | 11. mai 2018 : 00:45:13
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>}
 * @see <a href="https://rune-status.net/members/kris.354/">Rune-Status profile</a>}
 */
public interface ItemOnNPCAction extends Plugin {

	void handleItemOnNPCAction(final Player player, final Item item, final int slot, final NPC npc);
	
	Object[] getItems();
	
	Object[] getObjects();
	
	default void handle(final Player player, final Item item, final int slot, final NPC npc) {
		player.setRouteEvent(new EntityEvent(player, new EntityStrategy(npc), () -> {
			player.stopAll();
			player.faceEntity(npc);
			if (player.getInventory().getItem(slot) != item) {
				return;
			}
			handleItemOnNPCAction(player, item, slot, npc);
		}, true));
	}
	
}
