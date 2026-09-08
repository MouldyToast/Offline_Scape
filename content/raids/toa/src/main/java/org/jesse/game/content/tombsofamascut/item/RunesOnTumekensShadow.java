package org.jesse.game.content.tombsofamascut.item;

import org.jesse.game.item.Item;
import org.jesse.game.item.ids.ItemId;
import org.jesse.game.model.item.PairedItemOnItemPlugin;
import org.jesse.game.world.entity.player.Player;

@SuppressWarnings("unused")
public class RunesOnTumekensShadow implements PairedItemOnItemPlugin {

	@Override
	public void handleItemOnItemAction(Player player, Item from, Item to, int fromSlot, int toSlot) {
		final int fromId = from.getId();
		final Item staff;
		final int staffSlot;
		if (fromId == ItemId.TUMEKENS_SHADOW || fromId == ItemId.TUMEKENS_SHADOW_UNCHARGED) {
			staff = from;
			staffSlot = fromSlot;
		} else {
			staff = to;
			staffSlot = toSlot;
		}

		TumekensShadowPlugin.charge(player, staff, staffSlot);
	}

	@Override
	public ItemPair[] getMatchingPairs() {
		return new ItemPair[] {
				ItemPair.of(ItemId.SOUL_RUNE, ItemId.TUMEKENS_SHADOW),
				ItemPair.of(ItemId.CHAOS_RUNE, ItemId.TUMEKENS_SHADOW),
				ItemPair.of(ItemId.SOUL_RUNE, ItemId.TUMEKENS_SHADOW_UNCHARGED),
				ItemPair.of(ItemId.CHAOS_RUNE, ItemId.TUMEKENS_SHADOW_UNCHARGED),
		};
	}

}
