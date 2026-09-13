package org.jesse.plugins.itemonitem;

import org.jesse.game.content.skills.magic.Rune;
import org.jesse.game.item.Item;
import org.jesse.game.model.item.PairedItemOnItemPlugin;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.container.impl.RunePouch;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public final class RuneOnRunePouchItemAction implements PairedItemOnItemPlugin {
	@Override
	public void handleItemOnItemAction(final Player player, final Item from, final Item to, final int fromSlot, final int toSlot) {
		final int toId = to.getId();
		final int fromId = from.getId();
		final int runeId;
		final int runeSlot;
		final RunePouch runePouch;
		if (toId == RunePouch.RUNE_POUCH.getId() || toId == RunePouch.TOURNAMENT_RUNE_POUCH.getId() || toId == RunePouch.DIVINE_RUNE_POUCH.getId()) {
			runeId = fromId;
			runeSlot = fromSlot;
			runePouch = RunePouch.chooseRunePouch(player, toId);
		} else {
			runeId = toId;
			runeSlot = toSlot;
			runePouch = RunePouch.chooseRunePouch(player, fromId);
		}

		final Item rune = new Item(runeId, player.getInventory().getAmountOf(runeId));
		int amount = rune.getAmount();
		final int inPouch = runePouch.getAmountOf(runeId);
		if ((amount + (long) inPouch) >= 16000) {
			amount = 16000 - inPouch;
		}
		rune.setAmount(amount);
		if (amount <= 0) {
			player.sendMessage("You can't put that many runes in your pouch.");
			return;
		}
		final Rune r = Rune.getRune(rune);
		if (r == null) {
			player.sendMessage("You can only add runes to the rune pouch.");
			return;
		}
		final int capacity = runePouch.runePouchCapacity();
		if (runePouch.getContainer().getSize() == capacity) {
			if (runePouch.getAmountOf(runeId) == 0) {
				player.sendMessage("You can only carry " + (capacity == 3 ? "three" : "four") + " different types of runes in your rune pouch at a time.");
				return;
			}
		}
		runePouch.getContainer().deposit(player, player.getInventory().getContainer(), runeSlot, rune.getAmount());
		player.getInventory().refreshAll();
		runePouch.getContainer().refresh(player);
	}

		@Override
	public ItemPair[] getMatchingPairs() {
		final int runePouchId = RunePouch.RUNE_POUCH.getId();
		final int secondaryRunePouchId = RunePouch.TOURNAMENT_RUNE_POUCH.getId();
		final int divineRunePouchId = RunePouch.DIVINE_RUNE_POUCH.getId();
		final int length = Rune.values.length;
		final List<ItemPair> pairs = new ArrayList<>(length * 3);
		for (int i = 0; i < length; i++) {
			final int runeId = Rune.values[i].getId();
			pairs.add(new ItemPair(runePouchId, runeId));
			pairs.add(new ItemPair(secondaryRunePouchId, runeId));
			pairs.add(new ItemPair(divineRunePouchId, runeId));
		}
		return pairs.toArray(new ItemPair[0]);
	}
}
