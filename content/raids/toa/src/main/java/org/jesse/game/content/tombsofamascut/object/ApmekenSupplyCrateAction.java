package org.jesse.game.content.tombsofamascut.object;

import org.jesse.game.item.Item;
import org.jesse.game.item.ids.ItemId;
import org.jesse.game.world.entity.SoundEffect;
import org.jesse.game.world.entity.masks.Animation;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.world.object.WorldObject;

/**
 * @author Savions.
 */
public class ApmekenSupplyCrateAction implements ObjectAction {

	private static final Animation PICK_ANIMATION = new Animation(827);
	private static final SoundEffect PICK_SOUND = new SoundEffect(2582);

	@Override public void handleObjectAction(Player player, WorldObject object, String name, int optionId, String option) {
		final boolean hammer = object.getId() == 45497;
		final Item item = hammer ? new Item(ItemId.HAMMER, 1) : new Item(ItemId.NEUTRALISING_POTION, 100);
		if (player.getInventory().getFreeSlots() < 1 && (hammer || !player.getInventory().containsItem(ItemId.NEUTRALISING_POTION))) {
			player.sendMessage("You need more space in your inventory to do that.");
			return;
		}
		player.getInventory().addItem(item);
		player.sendMessage("You take " + (hammer ? "a hammer." : "some potions."));
		player.setAnimation(PICK_ANIMATION);
		player.sendSound(PICK_SOUND);
	}

	@Override public Object[] getObjects() {
		return new Object[] {45497, 45498};
	}
}
