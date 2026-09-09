package org.jesse.plugins.item;

import org.jesse.game.GameInterface;
import org.jesse.game.item.Item;
import org.jesse.game.item.ids.ItemId;
import org.jesse.game.model.item.pluginextensions.ItemPlugin;
import org.jesse.game.util.Colour;
import org.jesse.game.util.Utils;
import org.jesse.game.world.entity.player.Player;
import org.jesse.game.world.entity.player.container.impl.RunePouch;
import org.jesse.game.world.entity.player.dialogue.Dialogue;

/**
 * @author Kris | 25. aug 2018 : 22:43:48
 * @see <a href="https://www.rune-server.ee/members/kris/">Rune-Server profile</a>
 */
@SuppressWarnings("unused")
public class RunePouchItem extends ItemPlugin  {

	@Override
	public void handle() {
		bind("Open", (player, item, slotId) -> {
		    player.stopAll();
			player.addTemporaryAttribute("rune_pouch", item.getId());
            GameInterface.RUNE_POUCH.open(player);
		});
		bind("Empty", (player, item, slotId) -> {
			final int itemId = item.getId();
			final RunePouch runePouch = RunePouch.chooseRunePouch(player, itemId);
			runePouch.emptyRunePouch();
		});
		bind("Revert", (player, item, slotId) -> {
			if (item.getId() != RunePouch.DIVINE_RUNE_POUCH.getId()) {
				return;
			}

			if (!player.getInventory().hasFreeSlots()) {
				player.sendMessage("Not enough space in your inventory to revert Divine rune pouch into its original state.");
				return;
			}

			Item rune = player.getRunePouch().getRune(3);
			if (rune != null) {
				player.getDialogueManager().start(new Dialogue(player) {
					@Override
					public void buildDialogue() {
						plain("Your divine rune pouch has more runes in it than a regular rune pouch can contain. If you proceed with this you will lose " + Colour.MAROON.wrap(Utils.pluralizedFormatted(rune.getName(), rune.getAmount())) + ".");
						options("Revert your divine rune pouch?", new DialogueOption("Yes.", () -> revertDivine(player, item)), new DialogueOption("No."));
					}
				});
				return;
			}

			revertDivine(player, item);
		});
	}

	public static final Item THREAD_OF_ELIDINIS = new Item(ItemId.THREAD_OF_ELIDINIS);


	private static void revertDivine(final Player player, final Item item) {
		player.getDialogueManager().start(new Dialogue(player) {
			@Override
			public void buildDialogue() {
				if (!player.getInventory().containsItem(item)) {
					return;
				}

				player.getInventory().deleteItem(item);
				player.getInventory().addItem(THREAD_OF_ELIDINIS);
				player.getInventory().addItem(RunePouch.RUNE_POUCH);
				player.getRunePouch().getContainer().set(3, null);
				player.getRunePouch().getContainer().refresh(player);
				doubleItem(THREAD_OF_ELIDINIS, RunePouch.RUNE_POUCH, "You skillfully remove the Thread of Elidinis from the divine rune pouch reverting it to it's prior state.");
			}
		});
	}



	@Override
	public int[] getItems() {
		return new int[] { ItemId.RUNE_POUCH, ItemId.DIVINE_RUNE_POUCH };
	}

}
