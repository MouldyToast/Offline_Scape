package org.jesse.plugins.itemonitem;

import org.jesse.game.item.Item;
import org.jesse.game.item.ids.ItemId;
import org.jesse.game.model.item.ItemOnItemAction;
import org.jesse.game.world.entity.masks.Graphics;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.dialogue.Dialogue;
import org.jesse.plugins.dialogue.ItemChat;

/**
 * @author Savions.
 */
public class SaturatingImbuedHeartItemAction implements ItemOnItemAction {

	private static final Graphics GFX = new Graphics(2288);

	@Override public void handleItemOnItemAction(Player player, Item from, Item to, int fromSlot, int toSlot) {
		final Item essence = from.getId() == ItemId.ANCIENT_ESSENCE ? from : to;
		final Item imbuedHeart = from.getId() == ItemId.ANCIENT_ESSENCE ? to : from;
		player.getDialogueManager().start(new Dialogue(player) {
			@Override public void buildDialogue() {
				if (essence.getAmount() <= 150_000) {
					item(ItemId.SATURATED_HEART, "You'll need 150,000 ancient essence to saturate your imbued heart.");
					return;
				}
				plain("Do you wish to use 150,000 ancient essence to saturate your imbued heart?<br>This process is non-reversible.");
				options("Do you wish to saturate your imbued heart?", new DialogueOption("Yes.", () -> {
					player.getInventory().ifDeleteItem(new Item(essence.getId(), 150_000), () -> {
						player.getInventory().ifDeleteItem(imbuedHeart, () -> {
							final Item saturatedHeart = new Item(ItemId.SATURATED_HEART, 1);
							player.getInventory().addItem(saturatedHeart);
							player.setGraphics(GFX); //TODO find right animation
							player.getDialogueManager().start(new ItemChat(player, saturatedHeart, "You use 150,000 ancient essence to saturate your imbued heart, turning it into a saturated heart."));
						});
					});
				}), new DialogueOption("No."));
			}
		});
	}

	@Override public int[] getItems() {
		return new int[] { ItemId.ANCIENT_ESSENCE, ItemId.IMBUED_HEART };
	}
}
