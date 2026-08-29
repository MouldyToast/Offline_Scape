package com.zenyte.game.content.colosseum.items;

import com.near_reality.game.world.entity.player.PlayerAttributesKt;
import com.zenyte.game.GameInterface;
import com.zenyte.game.item.Item;
import com.zenyte.game.model.item.ItemOnItemAction;
import com.zenyte.game.model.item.pluginextensions.ChargeExtension;
import com.zenyte.game.model.item.pluginextensions.ItemPlugin;
import com.zenyte.game.model.ui.testinterfaces.EquipmentTabInterface;
import com.zenyte.game.util.Colour;
import com.zenyte.game.util.Utils;
import com.zenyte.game.world.entity.player.Player;
import com.zenyte.game.world.entity.player.calog.CATierType;
import com.zenyte.game.world.entity.player.container.ContainerWrapper;
import com.zenyte.game.world.entity.player.dialogue.Dialogue;
import com.zenyte.plugins.dialogue.DestroyItemDialogue;
import com.zenyte.plugins.dialogue.ItemChat;
import static com.zenyte.game.item.ItemId.*;

@SuppressWarnings("unused")
public class DizanaQuiverItemPlugin extends ItemPlugin implements ItemOnItemAction, ChargeExtension {

	private static final int BLESSING_PERCENT_PER_CHARGE = 1_500;
	public static final int CHARGES_FOR_BLESS = BLESSING_PERCENT_PER_CHARGE * 100;
	private static final int MAX_CHARGES = 20_000;
	public static final String BLESSSING_CHARGE_ATTRIBUTE = "dizanas_quiver_blessing_charge";

	@Override
	public void handle() {
		bind("Open", (player, item, container, slotId) -> {
			player.addTemporaryAttribute(DizanaQuiverInterface.OPENED_ITEM_ATTRIBUTE, item.getCharges());
			GameInterface.DIZANAS_QUIVER.open(player);
		});
		bind("Empty", (player, item, container, slotId) -> EquipmentTabInterface.handleDizanaQuiverRemoveOption(player));
		bind("Uncharge", (player, item, container, slotId) -> {
			int itemId = item.getId();
			int charges = item.getCharges();
			if (charges <= 0) {
				item.setId(chargedToUncharged(item));
				player.getInventory().refresh(slotId);
			} else {
				String chargeText = Utils.format(charges);
				int blessingCharge = item.getNumericAttribute(BLESSSING_CHARGE_ATTRIBUTE).intValue() / BLESSING_PERCENT_PER_CHARGE;
				player.getDialogueManager().start(new Dialogue(player) {
					@Override
					public void buildDialogue() {
						if (blessingCharge > 0) {
							options("Uncharging will lose you " + blessingCharge + "% progress towards blessing this quiver. Do you wish to proceed?", "No.", "Yes.").onOptionTwo(() -> {
								if (!player.getInventory().addItem(new Item(SUNFIRE_SPLINTERS, charges)).isFailure()) {
									player.getInventory().set(slotId, new Item(chargedToUncharged(item)));
									setKey(5);
								} else {
									setKey(10);
								}
							});
						} else {
							options("Uncharge Dizana's quiver?", "Yes.", "No.").onOptionOne(() -> {
								if (!player.getInventory().addItem(new Item(SUNFIRE_SPLINTERS, charges)).isFailure()) {
									player.getInventory().set(slotId, new Item(chargedToUncharged(item)));
									setKey(5);
								} else {
									setKey(10);
								}
							});
						}

						item(5, DIZANAS_QUIVER_UNCHARGED, "You fully uncharge Dizana's Quiver, regaining " + chargeText + " sunfire splinters in the process.");
						plain(10, "You do not have enough inventory space to uncharge Dizana's quiver.");
					}
				});
			}
		});
		bind("Check", (player, item, container, slotId) -> {
			int blessingCharge = item.getNumericAttribute(BLESSSING_CHARGE_ATTRIBUTE).intValue() / BLESSING_PERCENT_PER_CHARGE;
			player.sendMessage("Dizana's quiver has " + Utils.pluralizedFormattedColorized("charge", item.getCharges(), Colour.RED) + " remaining. It has made " + blessingCharge + "% progress towards being blessed.");
		});
		bind("Destroy", (player, item, container, slotId) -> player.getDialogueManager().start(new DestroyItemDialogue(player, item, slotId, () -> PlayerAttributesKt.setDizanasQuiver(player, -1, 0))));
	}

	@Override
	public int[] getItems() {
		return new int[]{DIZANAS_QUIVER_UNCHARGED, DIZANAS_QUIVER_UNCHARGED_L, DIZANAS_QUIVER, DIZANAS_QUIVER_L, BLESSED_DIZANAS_QUIVER, BLESSED_DIZANAS_QUIVER_L};
	}

	@Override
	public ItemPair[] getMatchingPairs() {
		return new ItemPair[]{
				new ItemPair(DIZANAS_QUIVER_UNCHARGED, SUNFIRE_SPLINTERS),
				new ItemPair(DIZANAS_QUIVER_UNCHARGED_L, SUNFIRE_SPLINTERS),
				new ItemPair(DIZANAS_QUIVER, SUNFIRE_SPLINTERS),
				new ItemPair(DIZANAS_QUIVER_L, SUNFIRE_SPLINTERS),
		};
	}

	@Override
	public void handleItemOnItemAction(Player player, Item from, Item to, int fromSlot, int toSlot) {
		final boolean toIsSplinters = to.getId() == SUNFIRE_SPLINTERS;
		final Item quiver = toIsSplinters ? from : to;
		final Item splinters = toIsSplinters ? to : from;
		final int quiverId = quiver.getId();
		if (quiver.getCharges() == MAX_CHARGES) {
			player.sendMessage("Your quiver is already fully charged.");
			return;
		}
		final int chargesToAdd = Math.min(MAX_CHARGES - quiver.getCharges(), splinters.getAmount());
		player.sendInputInt("How many charges do you wish to add to Dizana's quiver? (0 - " + chargesToAdd + ")", value -> {
			if (value < 1) {
				return;
			}
			final int addedCharges = Math.min(chargesToAdd, value);
			player.getInventory().ifDeleteItem(new Item(SUNFIRE_SPLINTERS, addedCharges), () -> {
				int idSwap = unchargedToCharged(quiver);
				if (idSwap != -1) {
					quiver.setId(idSwap);
					player.getInventory().refresh(toIsSplinters ? fromSlot : toSlot);
				}
				quiver.setCharges(quiver.getCharges() + addedCharges);
				player.getDialogueManager().start(new ItemChat(player, quiver, "You use " + addedCharges + " " + splinters.getName() + " to charge Dizana's quiver. It now has " + Utils.pluralizedFormattedColorized("charge", quiver.getCharges(), Colour.RED) + "."));
			});
		});
	}

	private static int chargedToBlessed(final Item item) {
		return switch (item.getId()) {
			case DIZANAS_QUIVER -> BLESSED_DIZANAS_QUIVER;
			case DIZANAS_QUIVER_L -> BLESSED_DIZANAS_QUIVER_L;
			default -> -1;
		};
	}

	private static int chargedToUncharged(final Item item) {
		return switch (item.getId()) {
			case DIZANAS_QUIVER -> DIZANAS_QUIVER_UNCHARGED;
			case DIZANAS_QUIVER_L -> DIZANAS_QUIVER_UNCHARGED_L;
			default -> -1;
		};
	}

	private static int unchargedToCharged(final Item item) {
		return switch (item.getId()) {
			case DIZANAS_QUIVER_UNCHARGED -> DIZANAS_QUIVER;
			case DIZANAS_QUIVER_UNCHARGED_L -> DIZANAS_QUIVER_L;
			default -> -1;
		};
	}

	@Override
	public void removeCharges(Player player, Item item, ContainerWrapper wrapper, int slotId, int amount) {
		if (player.getCombatAchievements().hasTierCompleted(CATierType.MASTER) && Utils.random(19) == 0) {
			player.sendFilteredMessage("As you've completed the master combat achievements, you have been prevented from losing a charge.");
			return;
		}

		// degrades of rate 1/3
		if (Utils.randomNoPlus(3) == 0) {
			return;
		}

		// Increase blessing of quiver
		int newBlessingCharge = item.getNumericAttribute(BLESSSING_CHARGE_ATTRIBUTE).intValue() + 1;
		if (newBlessingCharge >= CHARGES_FOR_BLESS) {
			item.setId(chargedToBlessed(item));
			item.resetAttributes();
			wrapper.refresh(slotId);
			player.sendMessage("<col=ef1020>Your " + item.getName() + " has become blessed!");
			return;
		} else {
			item.setAttribute(BLESSSING_CHARGE_ATTRIBUTE, newBlessingCharge);
		}

		int newCharges = Math.max(item.getCharges() - amount, 0);
		item.setCharges(newCharges);
		if (newCharges == 0) {
			int newId = chargedToUncharged(item);
			if (newId != -1) {
				player.sendMessage("<col=ef1020>Your " + item.getName() + " has ran out of charges.</col>");
				item.setId(newId);
				wrapper.refresh(slotId);
			}
		}
	}

}
