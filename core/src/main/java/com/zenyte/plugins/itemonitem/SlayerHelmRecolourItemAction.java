package com.zenyte.plugins.itemonitem;

import com.zenyte.game.item.Item;
import com.zenyte.game.item.ItemId;
import com.zenyte.game.model.item.ItemOnItemAction;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.plugins.item.SlayerHelm;
import it.unimi.dsi.fastutil.ints.IntArrayList;

import static com.zenyte.game.item.ItemId.*;

/**
 * @author Tommeh | 19 mei 2018 | 16:51:44
 * @see <a href="https://www.rune-server.ee/members/tommeh/">Rune-Server profile</a>}
 */
public final class SlayerHelmRecolourItemAction implements ItemOnItemAction {
	@Override
	public void handleItemOnItemAction(final Player player, final Item from, final Item to, final int fromSlot, final int toSlot) {
		final Item helmet = from.getId() == SLAYER_HELMET || from.getId() == SLAYER_HELMET_I ? from : to;
		final Item recolour = from.getId() != SLAYER_HELMET && from.getId() != SLAYER_HELMET_I ? from : to;
		for (final SlayerHelm.SlayerHelmRecolour r : SlayerHelm.SlayerHelmRecolour.values) {
			final String reward = r.getSlayerReward();
			final int base = r.getBase();
			if (recolour.getId() == base && (helmet.getId() == SLAYER_HELMET || helmet.getId() == SLAYER_HELMET_I)) {
				if (base != ARAXYTE_HEAD && !player.getSlayer().isUnlocked(reward)) {
					player.sendMessage("You need to unlock the slayer ability <col=00080>" + reward + "</col> before you can do this recolour.");
					return;
				}
				player.getInventory().deleteItemsIfContains(new Item[] {new Item(base), helmet}, () -> {
					player.getInventory().addItem(new Item(helmet.getId() == SLAYER_HELMET ? r.getHelm() : r.getHelmImbued()));
					player.sendMessage("You successfully recoloured your slayer helmet.");
				});
				return;
			}
		}
	}

	@Override
	public int[] getItems() {
		final IntArrayList list = new IntArrayList();
		for (final SlayerHelm.SlayerHelmRecolour recolour : SlayerHelm.SlayerHelmRecolour.values) {
			list.add(recolour.getBase());
		}
		list.add(SLAYER_HELMET);
		list.add(ItemId.SLAYER_HELMET_I);
		return list.toArray(new int[list.size()]);
	}
}
