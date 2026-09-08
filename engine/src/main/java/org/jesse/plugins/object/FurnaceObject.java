package org.jesse.plugins.object;

import org.jesse.game.content.skills.smithing.CannonballSmithing;
import org.jesse.game.item.ids.ItemId;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.container.impl.Inventory;
import org.jesse.game.world.entity.player.dialogue.DialogueManager;
import org.jesse.game.world.object.ObjectAction;
import org.jesse.game.world.object.WorldObject;
import org.jesse.plugins.dialogue.skills.CannonballSmithingD;
import org.jesse.plugins.dialogue.skills.MoltenGlassD;
import org.jesse.plugins.dialogue.skills.SmeltingD;

public final class FurnaceObject implements ObjectAction {
	@Override
	public void handleObjectAction(final Player player, final WorldObject object, final String name, final int optionId, final String option) {
		final DialogueManager dialogueManager = player.getDialogueManager();
		final Inventory inventory = player.getInventory();
		if (inventory.containsItem(ItemId.SODA_ASH) && inventory.containsItem(ItemId.BUCKET_OF_SAND)) {
			player.getDialogueManager().start(new MoltenGlassD(player));
		} else if ((inventory.containsItem(CannonballSmithing.MOULD) || inventory.containsItem(CannonballSmithing.MOULD)) && inventory.containsItem(ItemId.STEEL_BAR)) {
			dialogueManager.start(new CannonballSmithingD(player));
		} else {
			dialogueManager.start(new SmeltingD(player, object));
		}
	}

	@Override
	public Object[] getObjects() {
		return new Object[] {"Furnace", "Clay forge", "Lava forge", "Small furnace"};
	}
}
