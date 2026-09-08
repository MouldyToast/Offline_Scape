package org.jesse.plugins.itemonitem;

import org.jesse.game.content.skills.crafting.CraftingDefinitions;
import org.jesse.game.content.skills.crafting.CraftingDefinitions.AmuletStringingData;
import org.jesse.game.content.skills.crafting.actions.AmuletStringingCrafting;
import org.jesse.game.item.Item;
import org.jesse.game.model.item.ItemOnItemAction;
import org.jesse.game.world.entity.player.Player;

import java.util.ArrayList;

/**
 * @author Tommeh | 27 mei 2018 | 00:41:21
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public class AmuletStringingItemAction implements ItemOnItemAction {
	@Override
	public void handleItemOnItemAction(final Player player, final Item from, final Item to, final int fromSlot, final int toSlot) {
		final Item string = from.getId() == 1759 || from.getId() == 6038 ? from : to;
		AmuletStringingData data = string.getId() == 6038 ? AmuletStringingData.PRE_NATURE_AMULET : AmuletStringingData.VALUES.get(from.getId());
		if (data == null) {
			data = AmuletStringingData.VALUES.get(to.getId());
		}
		if (data == null) {
			player.sendMessage("Nothing interesting happens.");
			return;
		}
		player.getActionManager().setAction(new AmuletStringingCrafting(data));
	}

	@Override
	public int[] getItems() {
		return null;
	}

	@Override
	public ItemPair[] getMatchingPairs() {
		final ArrayList<ItemOnItemAction.ItemPair> list = new ArrayList<ItemPair>();
		for (final CraftingDefinitions.AmuletStringingData stringing : AmuletStringingData.VALUES_ARR) {
			list.add(new ItemPair(stringing.getMaterials()[0].getId(), stringing.getMaterials()[1].getId()));
		}
		return list.toArray(new ItemPair[list.size()]);
	}
}
