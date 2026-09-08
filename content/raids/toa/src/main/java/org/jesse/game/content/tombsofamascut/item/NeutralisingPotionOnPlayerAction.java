package org.jesse.game.content.tombsofamascut.item;

import org.jesse.game.content.tombsofamascut.encounter.ApmekenEncounter;
import org.jesse.game.item.Item;
import org.jesse.game.item.ids.ItemId;
import org.jesse.game.model.item.ItemOnPlayerPlugin;
import org.jesse.game.world.entity.player.Player;

/**
 * @author Savions.
 */
public class NeutralisingPotionOnPlayerAction implements ItemOnPlayerPlugin {

	@Override public void handleItemOnPlayerAction(Player player, Item item, int slot, Player target) {
		if (player.getArea() instanceof final ApmekenEncounter encounter) {
			encounter.usePotionOn(player, target);
		}
	}

	@Override public int[] getItems() {
		return new int[] {ItemId.NEUTRALISING_POTION};
	}
}
