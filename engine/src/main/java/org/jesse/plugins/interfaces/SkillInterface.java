package org.jesse.plugins.interfaces;

import org.jesse.game.content.skills.construction.constants.Furniture;
import org.jesse.game.item.Item;
import org.jesse.game.model.ui.UserInterface;
import org.jesse.game.model.ui.InterfacePosition;
import org.jesse.game.world.entity.player.Player;
import mgi.types.config.items.ItemDefinitions;

public final class SkillInterface implements UserInterface {

	@Override
	public void handleComponentClick(final Player player, final int interfaceId, final int componentId, final int slotId, final int itemId, final int optionId, final String option) {
		if (player.getTemporaryAttributes().get("viewingSkill") == null) {
			return;
		}
		final int modifier = (componentId - 13) * 1024;
		if (componentId == 13) {
			player.getVarManager().sendVar(965, (int) player.getTemporaryAttributes().get("viewingSkill"));
		} else if (componentId == 8) {
			final Furniture furn = Furniture.DISPLAYED_MAP.get(itemId);
			final ItemDefinitions defs = ItemDefinitions.get(itemId);
			if (furn == null) {
				player.sendMessage("Invalid furniture: " + itemId + "(" + (defs == null ? "null" : defs.getName()) + ")");
				return;
			}
			final StringBuilder builder = new StringBuilder();
			builder.append((defs == null ? "null" : defs.getName()) + ": ");
			for (final Item i : furn.getMaterials()) {
				String name = i.getName();
				if (name.equals("Bronze nails")) {
					name = "Nails";
				}
				builder.append("<col=ff0000>" + i.getAmount() + " x " + name + "</col>, ");
			}
			if (itemId == 8095) {
				builder.append("<col=ff0000>" + "1 x " + " of each of the four runes of the respective group" + "</col>, ");
			}
			builder.delete(builder.length() - 2, builder.length());
			player.sendMessage(builder.toString());
		} else if (componentId == 25 || componentId == 30) {
			player.getTemporaryAttributes().remove("viewingSkill");
			player.getInterfaceHandler().closeInterface(InterfacePosition.FLOATER);
		} else {
			player.getVarManager().sendVar(965, (int) player.getTemporaryAttributes().get("viewingSkill") + modifier);
		}

	}

	@Override
	public int[] getInterfaceIds() {
		return new int[] { 214 };
	}

}
