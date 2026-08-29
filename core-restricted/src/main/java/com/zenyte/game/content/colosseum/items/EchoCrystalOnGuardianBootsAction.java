package com.zenyte.game.content.colosseum.items;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.BossDropItem;
import com.zenyte.game.model.item.ItemOnItemAction;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

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
