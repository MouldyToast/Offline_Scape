package com.zenyte.game.content.colosseum.items;

import com.near_reality.game.world.entity.player.PlayerAttributesKt;
import com.zenyte.game.item.Item;
import com.zenyte.game.item.ids.ItemId;
import com.zenyte.game.model.item.ItemOnItemAction;
import com.zenyte.game.model.item.pluginextensions.ChargeExtension;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.util.Colour;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.calog.CATierType;
import com.zenyte.game.world.entity.player.container.ContainerWrapper;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.plugins.dialogue.ItemChat;

@SuppressWarnings("unused")
public class EchoBootsItemPlugin extends ItemPlugin implements ChargeExtension, ItemOnItemAction {

	private static final Item GUARDIAN_BOOTS = new Item(ItemId.GUARDIAN_BOOTS);
	private static final Item[] MATERIALS = {new Item(ItemId.ECHO_BOOTS)};

	@Override
	public void handle() {
		bind("check", (player, item, container, slotId) -> {
			if (PlayerAttributesKt.getEchoBootsActive(player)) {
				player.sendMessage("Your Echo boots have " + Utils.pluralizedFormattedColorized("charge", item.getCharges(), Colour.RS_PURPLE) + " remaining.");
			} else {
				player.sendMessage("Your Echo boots have " + Utils.pluralizedFormattedColorized("charge", item.getCharges(), Colour.RS_PURPLE) + " remaining. They are currently inactive.");
			}
		});
		bind("toggle-activity", (player, item, container, slotId) -> {
			boolean currentState = PlayerAttributesKt.getEchoBootsActive(player);
			PlayerAttributesKt.setEchoBootsActive(player, !currentState);
			if (PlayerAttributesKt.getEchoBootsActive(player)) {
				player.sendMessage("You activate your echo boots.");
			} else {
				player.sendMessage("You deactivate your echo boots.");
			}
		});
		bind("revert", (player, item, container, slotId) -> {
			player.getDialogueManager().start(new Dialogue(player) {
				@Override
				public void buildDialogue() {
					options("Revert your echo boots? Any charges will be lost.", "Yes.", "No.").onOptionOne(() -> {
						player.getInventory().deleteItemsIfContains(MATERIALS, () -> {
							player.getInventory().addOrDrop(GUARDIAN_BOOTS);
							setKey(5);
						});
					});

					item(5, ItemId.GUARDIAN_BOOTS, "You revert your echo boots into some guardian boots.");
				}
			});
		});
	}

	@Override
	public ItemPair[] getMatchingPairs() {
		return new ItemPair[]{new ItemPair(ItemId.ECHO_BOOTS, ItemId.ECHO_CRYSTAL)};
	}

	@Override
	public int[] getItems() {
		return new int[]{ItemId.ECHO_BOOTS};
	}

	@Override
	public void removeCharges(Player player, Item item, ContainerWrapper wrapper, int slotId, int amount) {
		if (player.getCombatAchievements().hasTierCompleted(CATierType.MASTER) && Utils.random(19) == 0) {
			player.sendFilteredMessage("As you've completed the master combat achievements, you have been prevented from losing a charge.");
			return;
		}

		int newCharges = Math.max(item.getCharges() - amount, 0);
		item.setCharges(newCharges);
		if (newCharges == 0) {
			player.sendMessage("<col=ef1020>Your " + item.getName() + " has ran out of charges.</col>");
		}
	}

	@Override
	public void handleItemOnItemAction(Player player, Item from, Item to, int fromSlot, int toSlot) {
		final boolean toIsEcho = to.getId() == ItemId.ECHO_CRYSTAL;
		final Item boots = toIsEcho ? from : to;
		final Item crystal = toIsEcho ? to : from;
		if (boots.getCharges() >= 60_000) {
			player.sendMessage("Your boots are already fully charged.");
			return;
		}

		final int chargesToAdd = Math.min(Math.max(1, (60_000 - boots.getCharges()) / 6_000), player.getInventory().getAmountOf(crystal.getId()));
		player.sendInputInt("How many crystals do you wish to add to your " + boots.getName() + "? (0 - " + chargesToAdd + ")", value -> {
			if (value < 1) {
				return;
			}

			final int addedCharges = Math.min(chargesToAdd, value) * 6_000;
			player.getInventory().ifDeleteItem(new Item(ItemId.ECHO_CRYSTAL, value), () -> {
				boots.setCharges(Math.min(60_000, boots.getCharges() + addedCharges));
				player.getDialogueManager().start(new ItemChat(player, boots, "You use " + value + " " + crystal.getName() + " to charge your " + boots.getName() + ". It now has " + Utils.pluralizedFormattedColorized("charge", boots.getCharges(), Colour.RED) + "."));
			});
		});
	}

}
