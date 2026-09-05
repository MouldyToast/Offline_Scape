package com.zenyte.game.content.colosseum;

import com.zenyte.game.GameInterface;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.ui.Interface;
import com.zenyte.game.util.ItemUtil;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.container.Container;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;

@SuppressWarnings("unused")
public class ColosseumRewardChestInterface extends Interface {

	@Override
	protected void attach() {
		put(5, "bank-all");
		put(7, "take-all");
		put(9, "discard-all");
		put(11, "take");
	}

	@Override
	public void build() {
		bind("bank-all", player -> {
			Container container = getContainer(player);
			if (container == null) return;

			for (int slot = 0; slot < container.getContainerSize() + 1; slot++) {
				Item item = container.get(slot);
				if (item == null) continue;
				player.getBank().getContainer().deposit(player, container, slot, item.getAmount());
			}
			container.refresh(player);
			player.getBank().getContainer().refresh(player);
			setValue(player, container);
		});

		bind("take-all", player -> {
			Container container = getContainer(player);
			if (container == null) return;

			for (int slot = 0; slot < container.getContainerSize() + 1; slot++) {
				Item item = container.get(slot);
				if (item == null) continue;

				container.set(slot, item.toNote()); // Note on withdraw
				player.getInventory().getContainer().deposit(player, container, slot, item.getAmount());

				if (container.get(slot) != null) {
					container.set(slot, new Item(item.getDefinitions().getUnnotedOrDefault(), item.getAmount()));
				}
			}

			container.refresh(player);
			player.getInventory().getContainer().refresh(player);
			setValue(player, container);
		});

		bind("discard-all", player -> player.getDialogueManager().start(new Dialogue(player) {
			@Override
			public void buildDialogue() {
				options("Are you sure you want to destroy the items?",
						new DialogueOption("<col=ff0000>DESTROY!", () -> {
							Container container = getContainer(player);
							if (container != null) {
								container.clear();
								container.refresh(player);
								setValue(player, container);
							}
						}), new DialogueOption("Cancel"));
			}
		}));

		bind("take", (player, slotId, itemId, option) -> {
			if (option == 10) {
				ItemUtil.sendItemExamine(player, itemId);
				return;
			}

			Container container = getContainer(player);
			if (container == null) {
				return;
			}

			Item item = container.get(slotId);
			if (item == null) return;

			container.set(slotId, item.toNote());
			container.withdraw(player, player.getInventory().getContainer(), slotId, item.getAmount());

			if (container.get(slotId) != null) {
				container.set(slotId, new Item(item.getDefinitions().getUnnotedOrDefault(), item.getAmount()));
			}

			container.refresh(player);
			player.getInventory().getContainer().refresh(player);
			setValue(player, container);
		});
	}

	private Container getContainer(Player player) {
		if (!(player.getArea() instanceof ColosseumInstance instance)) {
			return null;
		}

		Container container = instance.getRewards();
		if (container.isEmpty()) {
			player.sendMessage("Your reward chest is empty.");
			return null;
		}

		return container;
	}

	@Override
	public GameInterface getInterface() {
		return GameInterface.COLOSSEUM_REWARDS;
	}

	public static void setValue(Player player, Container container) {
		player.getPacketDispatcher().sendComponentText(GameInterface.COLOSSEUM_REWARDS, 3, "Total Value: " + Utils.formatNumberWithCommas(container.calculateValue()) + " GP");
	}

}
