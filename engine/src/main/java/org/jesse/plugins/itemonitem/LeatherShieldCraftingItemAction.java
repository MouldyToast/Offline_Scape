package org.jesse.plugins.itemonitem;

import org.jesse.game.content.skills.crafting.CraftingDefinitions;
import org.jesse.game.content.skills.crafting.CraftingDefinitions.LeatherShieldData;
import org.jesse.game.content.skills.smithing.Smithing;
import org.jesse.game.item.Item;
import org.jesse.game.model.item.ItemOnItemAction;
import org.jesse.game.world.entity.player.Player;
import org.jesse.plugins.dialogue.skills.LeatherShieldCraftingD;
import it.unimi.dsi.fastutil.ints.IntArrayList;

/**
 * @author Tommeh | 14 apr. 2018 | 17:46:14
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server
 *      profile</a>}
 */
public final class LeatherShieldCraftingItemAction implements ItemOnItemAction {
	@Override
	public void handleItemOnItemAction(final Player player, final Item from, final Item to, final int fromSlot, final int toSlot) {
		if (LeatherShieldData.isMaterial(from, to)) {
			final CraftingDefinitions.LeatherShieldData data = LeatherShieldData.getDataByMaterials(from, to);



			if (data != null) {
				player.getDialogueManager().start(new LeatherShieldCraftingD(player, data));
			}
			return;
		}
	}

	@Override
	public int[] getItems() {
		final IntArrayList list = new IntArrayList();
		for (final CraftingDefinitions.LeatherShieldData data : LeatherShieldData.VALUES) {
			for (final Item item : data.getMaterials()) {
				list.add(item.getId());
			}
		}
		list.add(Smithing.HAMMER.getId());
		return list.toArray(new int[list.size()]);
	}
}
