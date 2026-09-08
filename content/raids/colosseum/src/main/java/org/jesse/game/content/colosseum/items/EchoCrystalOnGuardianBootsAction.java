package org.jesse.game.content.colosseum.items;

import org.jesse.game.item.Item;
import org.jesse.game.item.ids.ItemId;
import org.jesse.game.model.item.BossDropItem;
import org.jesse.game.model.item.ItemOnItemAction;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.dialogue.Dialogue;

@SuppressWarnings("unused")
public class EchoCrystalOnGuardianBootsAction implements ItemOnItemAction {

	private static final Item ECHO_BOOTS = new Item(ItemId.ECHO_BOOTS, 1, 6_000);
	private static final Item[] MATERIALS = {
			new Item(ItemId.GUARDIAN_BOOTS), new Item(ItemId.ECHO_CRYSTAL)
	};

	@Override
	public void handleItemOnItemAction(Player player, Item from, Item to, int fromSlot, int toSlot) {
		player.getDialogueManager().start(new Dialogue(player) {
			@Override
			public void buildDialogue() {
				options("Select an Option", "Combine the crystal with your boots.", "Don't combine.").onOptionOne(() -> {
					player.getInventory().deleteItemsIfContains(MATERIALS, () -> player.getInventory().addItem(ECHO_BOOTS));
				});
			}
		});
	}

	@Override
	public int[] getItems() {
		return new int[]{ItemId.GUARDIAN_BOOTS, ItemId.ECHO_CRYSTAL};
	}

}
