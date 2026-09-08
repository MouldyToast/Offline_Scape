package org.jesse.plugins.item;

import org.jesse.game.content.rots.RotsManager;
import org.jesse.game.item.Item;
import org.jesse.game.model.item.pluginextensions.ItemPlugin;
import org.jesse.game.world.entity.player.dialogue.Dialogue;

public class BarrowsTotem extends ItemPlugin {

	@Override
	public void handle() {
		bind("Activate", (player, item, slotId) -> {
			player.getDialogueManager().start(new Dialogue(player) {
				@Override
				public void buildDialogue() {
					item(new Item(32168), "Totem starts glowing, activating it will teleport you somewhere.");
					options(new DialogueOption("Consume the totem.", () -> {
						if (!player.getInventory().deleteItem(new Item(32168)).isFailure()) {
							RotsManager.createInstance(player);
						}
					}), new DialogueOption("Leave."));
				}
			});
		});
	}

	@Override
	public int[] getItems() {
		return new int[] { 32168 };
	}

}
